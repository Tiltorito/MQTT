import mqtt.API.Topic;
import mqtt.API.TopicPub;
import mqtt.API.TopicSub;
import mqtt.utilities.logger.Logger;
import smarthome.Server;

import java.util.ArrayDeque;
import java.util.Deque;
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
        server.send("station/topic", "hello there folks1");

        Thread.sleep(1000);

        ArrayDeque<String> queue = server.getMessagesFromTopic("station/topic");


        System.out.println(queue.pop());

        server.close();

    }
}
