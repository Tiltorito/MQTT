package mqtt.utilities.command;

/**
 * Created by harry on 30/01/2018.
 */

public class InvalidCommandException extends RuntimeException {
    public InvalidCommandException() {
        super();
    }

    public InvalidCommandException(String message) {
        super(message);
    }
}
