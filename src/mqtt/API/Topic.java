package mqtt.API;

import mqtt.utilities.StringUtilities;
import mqtt.utilities.command.CommandBuilder;
import mqtt.utilities.command.InvalidCommandException;
import mqtt.utilities.logger.BlockFailedException;
import mqtt.utilities.logger.Logger;

import java.io.*;
import java.util.ArrayDeque;

/**
 * Created by harry on 30/01/2018.
 */

public class Topic {
    private static Logger logger = new Logger(Topic.class);

    private String hostname; // host name
    private String topic; // topic name
    private String topicMsg; // debug perpouses

    private Process processSub; // process to read messages
    private Process shellProcess; // a shell process, will be used to send messages.

    private DataOutputStream shellProcessOutputStream;

    private ArrayDeque<String> incomingMessages = new ArrayDeque<>();

    private Thread listener;

    public Topic(String hostname, String topic) {
        this.hostname = hostname;
        this.topic = topic;
        this.topicMsg = StringUtilities.addBrackets(topic) + " topic";

        String[] args = {"/bin/bash", "-c", new CommandBuilder()
                .setHostname(hostname)
                .setTopic(topic).build()
        };

        processSub = logger.withDebugLogs("subscription process for " + topicMsg,
                () -> new ProcessBuilder(args).start());

        // starting a new shell process, to send messages.
        shellProcess = logger.withDebugLogs("shell for " + topicMsg,
                () -> new ProcessBuilder("/bin/bash").start());

        shellProcessOutputStream = new DataOutputStream(shellProcess.getOutputStream());

    }


    /**
     * Sends a message in the topic.
     * @param msg the message to be sent
     * @return true if the message has been sent successfully, false if there was an error
     */
    public boolean send(String msg) {
        try {
            return logger.withInfoLogs("Sending message: " + msg + " -- on " + topicMsg, () -> {
                shellProcessOutputStream.writeUTF(new CommandBuilder()
                        .setHostname(hostname)
                        .setTopic(topic)
                        .setMessage(msg)
                        .build());
                return true;
            });
        }
        catch(BlockFailedException e) {
            e.printStackTrace();
            return false;
        }
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
        return !incomingMessages.isEmpty();
    }

    /**
     * Returns a copy of the incoming messages.
     * @return a copy of the incoming messages
     */
    public ArrayDeque<String> getIncomingMessages() {
        return incomingMessages.clone();
    }

    /**
     * Returns the hostname.
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Returns the name of the topic.
     * @return the name of the topic
     */
    public String getTopic() {
        return topic;
    }


    /**
     * A thread which checks if it has any messages from the sub and put these messages in the queue
     */
    private static class ListenForIncomingMessages extends Thread {
        private static Logger logger = new Logger(ListenForIncomingMessages.class);

        private ArrayDeque<String> queue; // the queue that the responses will be added

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
                    queue.offer(response);
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
