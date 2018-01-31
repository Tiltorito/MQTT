package mqtt.utilities;

import java.util.Date;

/**
 * Created by harry on 30/01/2018.
 */

public class StringUtilities {
    public static boolean isEmpty(String... arr) {
        for(String str : arr) {
            if(str == null || str.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public static String addBrackets(String str) {
        return "[" + str + "]";
    }

    public static String addTimeStamp(String msg) {
        return msg + " -- " + new Date().toString();
    }
}
