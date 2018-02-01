package mqtt.API;

import mqtt.utilities.command.CommandBuilder;
import mqtt.utilities.logger.BlockFailedException;
import mqtt.utilities.logger.Logger;

import java.io.DataOutputStream;

/**
 * Created by harry on 01/02/2018.
 */

public class TopicPub extends Topic {
    private static Logger logger = new Logger(TopicPub.class);

    private Process shellProcess; // a shell process, will be used to send messages.
    private DataOutputStream shellProcessOutputStream;


    public TopicPub(String hostname, String topic) {
        super(hostname, topic);

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
}