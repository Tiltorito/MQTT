package mqtt.API;

import mqtt.utilities.StringUtilities;

/**
 * Created by harry on 01/02/2018.
 */

public abstract class Topic {
    protected String hostname; // host name
    protected String topic; // topic name
    protected String topicMsg; // debug perpouses


    public Topic(String hostname, String topic) {
        this.hostname = hostname;
        this.topic = topic;
        this.topicMsg = StringUtilities.addBrackets(topic) + " topic";
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

    public abstract boolean close();
}
