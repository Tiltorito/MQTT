package mqtt.utilities;

import java.util.Date;

/**
 * Created by harry on 30/01/2018.
 */

/**
 * StringUtilities class provides useful String utilities that they are used across the project
 */
public class StringUtilities {

    /**
     * Takes a variable length of String and returns true if at least one of them is null or empty.
     * It returns false if and only if all the arguments are neither null or empty.
     * @param arr the arguments
     * @return true if at least one of them is null or empty, it returns false if and only if all the arguments are neither null or empty.
     */
    public static boolean isEmpty(String... arr) {
        for(String str : arr) {
            if(str == null || str.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds brackets around a String (eg I am a String -> [I am a String]).
     * @param str the String
     * @return a new String with brackets around the String which passed as argument.
     */
    public static String addBrackets(String str) {
        return "[" + str + "]";
    }


    public static String addChar(String str, char ch) {
        return String.valueOf(ch) + str + ch;
    }

    /**
     * Adds a timeStamp in the end of a string.
     * @param msg the String that the timestamp will be added
     * @return a new String containing a timestamp in the end of String passed as argument
     */
    public static String addTimeStamp(String msg) {
        return msg + " -- " + new Date().toString();
    }
}
