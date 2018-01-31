package mqtt.utilities.logger;

/**
 * Created by harry on 30/01/2018.
 */

interface BaseLogger {
    boolean e(String msg);
    boolean d(String msg);
    boolean i(String msg);
    boolean w(String msg);
    boolean close();
}
