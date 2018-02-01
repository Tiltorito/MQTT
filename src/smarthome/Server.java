package smarthome;

import mqtt.API.Topic;
import mqtt.API.TopicPub;
import mqtt.API.TopicSub;
import mqtt.utilities.logger.BlockFailedException;
import mqtt.utilities.logger.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;

/**
 * Created by harry on 30/01/2018.
 */

public class Server {
    private Logger logger = new Logger(Server.class);

    private HashMap<String, TopicSub> subTopics = new HashMap<>();
    private HashMap<String, TopicPub> pubTopics = new HashMap<>();

    private String localhost;

    public Server(String localhost) {
        this.localhost = localhost;
    }

    public boolean addSubTopic(String topic) {
        try {
            logger.withInfoLogs("adding " + topic + " in the list of subscribed topics"
                    , "to add topic, server is already subscribed to this topic"
                    , () -> {
                        if (subTopics.containsKey(topic)) {
                            throw new RuntimeException();
                        }

                        TopicSub sub = new TopicSub(localhost, topic);
                        subTopics.put(topic, sub);
                        sub.startListening();
                    });
        }
        catch(BlockFailedException e) {
            return false;
        }

        return true;
    }

    public boolean addPubTopic(String topic) {
        try {
            logger.withInfoLogs("adding " + topic + " in the list of pub topics"
                    , "this topic is already in the list of pub topics"
                    , () -> {
                        if (pubTopics.containsKey(topic)) {
                            throw new RuntimeException();
                        }

                        TopicPub pub = new TopicPub(localhost, topic);
                        pubTopics.put(topic, pub);
                    });
        }
        catch(BlockFailedException e) {
            return false;
        }

        return true;
    }

    public boolean send(String topic, String message) {
        try {
            logger.withInfoLogs("sending " + message + " at topic " + topic
                    , topic + " is not registered on the server"
                    , () -> pubTopics.get(topic).send(message));
        }
        catch(BlockFailedException e) {
            return false;
        }

        return true;
    }

    public ArrayDeque<String> getMessagesFromTopic(String topic) {
        try {
            return logger.withInfoLogs("collecting incoming messages from " + topic
                    , topic + " couldn't be found"
                    , () -> subTopics.get(topic).getIncomingMessages());
        }
        catch(BlockFailedException e) {
            return new ArrayDeque<>();
        }

    }

    public void close() {
        for(Topic topic : subTopics.values()) {
            logger.withInfoLogs("Clossing sub " + topic, "failed to close sub " + topic, () -> topic.close());
        }

        for(Topic topic : subTopics.values()) {
            logger.withInfoLogs("Clossing pub " + topic, "failed to close pub " + topic, () -> topic.close());
        }
    }
}
