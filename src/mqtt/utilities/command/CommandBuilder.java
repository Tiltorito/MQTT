package mqtt.utilities.command;

import mqtt.utilities.StringUtilities;

/**
 * Created by harry on 30/01/2018.
 */

public class CommandBuilder {
    private String hostname;
    private String topic;
    private String message;

    public CommandBuilder setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public CommandBuilder setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public CommandBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public String build() throws InvalidCommandException {
        if(StringUtilities.isEmpty(hostname, topic)) {
            throw new InvalidCommandException("Wrong Syntax for command");
        }

        StringBuilder builder = new StringBuilder();

        if(StringUtilities.isEmpty(message)) {
            builder.append("mosquitto_sub");
        }
        else {
            builder.append("mosquitto_pub");
        }

        builder.append(" -h ").append(hostname);
        builder.append(" -t ").append(topic);

        if(!StringUtilities.isEmpty(message)) {
            builder.append(" -m ").append(StringUtilities.addChar(message, '"'));
        }

        return builder.toString();
    }
}
