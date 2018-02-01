import mqtt.API.Topic;
import mqtt.API.TopicPub;
import mqtt.API.TopicSub;
import mqtt.utilities.logger.Logger;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by harry on 30/01/2018.
 */

public class Solution {
    public static void main(String[] args) throws Exception {
        Logger logger = new Logger(Solution.class);

        TopicSub sub = new TopicSub("localhost","topic/station");
        sub.startListening();

        TopicPub pub = new TopicPub("localhost", "topic/station");

        for(int i = 1; i <= 10; i++) {
            pub.send("This is the " + i + " message");
            Thread.sleep(1000);
        }

    }
}
