package mqtt.utilities;

import mqtt.utilities.logger.BlockFailedException;
import mqtt.utilities.logger.Logger;

/**
 * Created by harry on 01/02/2018.
 */

public class Utilities {

    /**
     * Kills a process while writing logs and handling all the exceptions that might occurred.
     * It takes 1 sec to finish because is checking the process status.
     * @param process the process to be killed
     * @param logger the logger to be used to write logs
     * @param description description message to be passed on logger
     * @return true if the process is killed successfully, false if the process is still alive
     */
    public static boolean killProcess(Process process, Logger logger, String description) {
        process.destroyForcibly();
        try {
            return logger.withInfoLogs(description, () -> {
                Thread.sleep(1000); // give it some time

                if (process.isAlive()) {
                    throw new RuntimeException("Couldn't terminate the process");
                }

                return true;
            });
        }
        catch(BlockFailedException e) {
            return false;
        }
    }
}
