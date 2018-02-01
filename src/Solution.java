import mqtt.API.Topic;
import mqtt.API.TopicPub;
import mqtt.API.TopicSub;
import mqtt.utilities.logger.Logger;
import smarthome.Server;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by harry on 30/01/2018.
 */

public class Solution {
    public static void main(String[] args) throws Exception {
        Logger logger = new Logger(Solution.class);

        Server server = new Server("localhost");

        server.addPubTopic("station/topic");
        server.addSubTopic("station/topic");

        server.send("station/topic", "hello there folks");
    }
}
