package mqtt.utilities.logger;

/**
 * Created by harry on 30/01/2018.
 */

public class BlockFailedException extends RuntimeException {
    public BlockFailedException() {
        super();
    }

    public BlockFailedException(String message) {
        super(message);
    }
}
