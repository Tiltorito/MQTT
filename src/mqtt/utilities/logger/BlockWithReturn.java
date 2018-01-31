package mqtt.utilities.logger;

/**
 * Created by harry on 30/01/2018.
 */

public interface BlockWithReturn<T> {
    T run() throws Exception;
}
