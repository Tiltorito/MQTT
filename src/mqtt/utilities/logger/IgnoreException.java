package mqtt.utilities.logger;

/**
 * Created by harry on 30/01/2018.
 */

public class IgnoreException {
    public static boolean execute(Block block) {
        try {
            block.run();
        }
        catch(Exception e) {
            return false;
        }

        return true;
    }
}
