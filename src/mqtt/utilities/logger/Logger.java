package mqtt.utilities.logger;

import java.util.function.Consumer;

/**
 * Created by harry on 30/01/2018.
 */

public class Logger implements BaseLogger {
    private final String tag;


    private static final String ERROR_TAG = "[ERROR]";
    private static final String INFO_TAG = "[INFO]";
    private static final String DEBUG_TAG = "[DEBUG]";
    private static final String WARNING_TAG = "[WARNING]";

    private static BaseLogger logger = new FileLogger.FileLoggerBuilder()
            .setInfoFile("infoLogs.txt")
            .setDebugFile("debugLogs.txt")
            .setErrorFile("errorLogs.txt")
            .setWarnFile("warningLogs.txt")
            .build();

    public Logger(Class<?> klass) {
        this(klass.getSimpleName());
    }

    private Logger(String tag) {
        this.tag = tag;
    }

    /**
     * Printing in the console and writing the message in the error log file.
     * @param msg the message to be written
     * @return true if the message has been written successfully, false if something went wrong
     */
    @Override
    public boolean e(String msg) {
        String str = addTag(msg);
        System.out.println(ERROR_TAG + str);

        return logger.e(str);
    }

    /**
     * Printing in the console and writing the message in the debug log file.
     * @param msg the message to be written
     * @return true if the message has been written successfully, false if something went wrong
     */
    @Override
    public boolean d(String msg) {
        String str = addTag(msg);
        System.out.println(DEBUG_TAG + str);

        return logger.d(str);
    }

    /**
     * Printing in the console and writing the message in the info log file.
     * @param msg the message to be written
     * @return true if the message has been written successfully, false if something went wrong
     */
    @Override
    public boolean i(String msg) {
        String str = addTag(msg);
        System.out.println(INFO_TAG + str);

        return logger.i(str);
    }

    /**
     * Printing in the console and writing the message in the warning log file.
     * @param msg the message to be written
     * @return true if the message has been written successfully, false if something went wrong
     */
    @Override
    public boolean w(String msg) {
        String str = addTag(msg);
        System.out.println(WARNING_TAG + str);

        return logger.w(str);
    }

    /**
     * Executes a block of code while writing comments anywhere around it, ie,
     * Starting loading files
     * Completed loading files OR
     * Failed loading files if an exception is be thrown in the block which passed as argument.
     * The logs are error logs.
     * @param description the description message that will be used for the logs
     * @param block the block to be executed
     * @param <T> the return type of the block
     * @return the return value of the block
     * @throws BlockFailedException if the block throws an exception
     */
    public <T> T withErrorLogs(String description, BlockWithReturn<T> block) throws BlockFailedException {
        return withLogging(this::e, description, block);
    }

    /**
     * Executes a block of code while writing comments anywhere around it, ie,
     * Starting loading files
     * Completed loading files OR
     * Failed loading files if an exception is be thrown in the block which passed as argument.
     * The logs are debug logs.
     * @param description the description message that will be used for the logs
     * @param block the block to be executed
     * @param <T> the return type of the block
     * @return the return value of the block
     * @throws BlockFailedException if the block throws an exception
     */
    public <T> T withDebugLogs(String description, BlockWithReturn<T> block) throws BlockFailedException {
        return withLogging(this::d, description, block);
    }

    /**
     * Executes a block of code while writing comments anywhere around it, ie,
     * Starting loading files
     * Completed loading files OR
     * Failed loading files if an exception is be thrown in the block which passed as argument.
     * The logs are info logs.
     * @param description the description message that will be used for the logs
     * @param block the block to be executed
     * @param <T> the return type of the block
     * @return the return value of the block
     * @throws BlockFailedException if the block throws an exception
     */
    public <T> T withInfoLogs(String description, BlockWithReturn<T> block) throws BlockFailedException {
        return withLogging(this::i, description, block);
    }

    /**
     * Executes a block of code while writing comments anywhere around it, ie,
     * Starting loading files
     * Completed loading files OR
     * Failed loading files if an exception is be thrown in the block which passed as argument.
     * The logs are warning logs.
     * @param description the description message that will be used for the logs
     * @param block the block to be executed
     * @param <T> the return type of the block
     * @return the return value of the block
     * @throws BlockFailedException if the block throws an exception
     */
    public <T> T withWarningLogs(String description, BlockWithReturn<T> block) throws BlockFailedException {
        return withLogging(this::w, description, block);
    }


    private <T> T withLogging(Consumer<String> method, String description, BlockWithReturn<T> block) throws BlockFailedException {
        method.accept("Starting " + description);

        T res = null;

        try {
            res = block.run();
            method.accept("Completed " + description);
        }
        catch(Exception e) {
            method.accept("Failed " + description);
            throw new BlockFailedException(e.getMessage());
        }

        return res;
    }

    /**
     * Closing all the log files that is used by the logger.
     * @return true if everything have been closed successfully, false if at least one file couldn't close or threw an exception
     */
    @Override
    public boolean close() {
        return logger.close();
    }

    private String addTag(String msg) {
        return tag + " -- " + msg;
    }
}

