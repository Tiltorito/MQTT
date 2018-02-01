package smarthome;

import mqtt.API.TopicSub;
import mqtt.utilities.logger.Logger;

import java.util.HashMap;

/**
 * Created by harry on 30/01/2018.
 */

public class Server {
    private Logger logger = new Logger(Server.class);

    private HashMap<String, TopicSub> subTopics = new HashMap<>();
    private HashMap<String, TopicSub> pubTopics = new HashMap<>();

    private String localhost;

    public Server(String localhost) {
        this.localhost = localhost;
    }

    public boolean addSubTopic(String topic) {

        if(subTopics.containsKey(topic)) {
            logger.e("Duplicate topic, server is already subscribed to this topic");
            return false;
        }

        TopicSub sub = new TopicSub(localhost, topic);
        subTopics.put(topic, sub);

        return true;
    }

    public boolean addPubTopic(String topic) {
        return false;
    }
}
