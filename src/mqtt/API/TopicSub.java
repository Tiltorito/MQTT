package mqtt.API;

import mqtt.utilities.StringUtilities;
import mqtt.utilities.command.CommandBuilder;
import mqtt.utilities.logger.BlockFailedException;
import mqtt.utilities.logger.Logger;

import java.io.*;
import java.util.ArrayDeque;

/**
 * Created by harry on 30/01/2018.
 */

public class TopicSub extends Topic {
    private static Logger logger = new Logger(TopicSub.class);

    private Process processSub; // process to read messages

    private final ArrayDeque<String> incomingMessages = new ArrayDeque<>();

    private Thread listener;

    public TopicSub(String hostname, String topic) {
        super(hostname, topic);

        String[] args = {"/bin/bash", "-c", new CommandBuilder()
                .setHostname(hostname)
                .setTopic(topic).build()
        };

        processSub = logger.withDebugLogs("subscription process for " + topicMsg,
                () -> new ProcessBuilder(args).start());
    }

    /**
     * Starts the listening thread.
     * @return true if the listening thread started successfully, false if there was a problem or it was already running
     */
    public boolean startListening() {
        if(listener == null && processSub != null) {
            listener = new ListenForIncomingMessages(topicMsg, incomingMessages, processSub.getInputStream());
            listener.start();

            return true;
        }

        return false;
    }

    /**
     * Stops the current listening thread.
     * @return true if indeed this method call stopped the thread, false if it was already stopped
     */
    public boolean stopListening() {
        if(listener != null) {
            listener.interrupt();
            listener = null;

            return true;
        }

        return false; // it is already stopped
    }

    /**
     * Retrus true if the listening thread is running, false otherwise.
     * @return true if the listening thread is running, false otherwise
     */
    public boolean isListening() {
        return listener != null;
    }

    /**
     * Returns true if there is incoming messages in the queue, false otherwise.
     * @return true if there is incoming messages in the queue, false otherwise
     */
    public boolean hasIncomingMessages() {
        boolean hasMessages = false;

        synchronized (incomingMessages) {
             hasMessages = !incomingMessages.isEmpty();
        }

        return hasMessages;
    }

    /**
     * Returns a copy of the incoming messages.
     * @return a copy of the incoming messages
     */
    public ArrayDeque<String> getIncomingMessages() {
        ArrayDeque<String> messages = null;

        synchronized (incomingMessages) {
            messages = incomingMessages.clone();
        }

        return messages;
    }


    /**
     * A thread which checks if it has any messages from the sub and put these messages in the queue
     */
    private static class ListenForIncomingMessages extends Thread {
        private static Logger logger = new Logger(ListenForIncomingMessages.class);

        private final ArrayDeque<String> queue; // the queue that the responses will be added

        private BufferedReader stream; // the stream to read from

        private String topicName; // the name of the topic, used for debug perpouses

        ListenForIncomingMessages(String topicName, ArrayDeque<String> queue, InputStream stream) {
            this.topicName = topicName;
            this.queue = queue;
            this.stream = new BufferedReader(new InputStreamReader(stream));
        }

        @Override
        public void run() {
            while(!isInterrupted()) {
                try {
                    String response = stream.readLine();

                    synchronized (queue) {
                        queue.offer(response);
                    }

                    logger.d("Read: " + response + " - from " + topicName);
                }
                catch(IOException e) {
                    e.printStackTrace();
                    logger.e("Problem occurred while reading from " + topicName);
                }
            }
        }


    }
}
