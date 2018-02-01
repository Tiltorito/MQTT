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

    private static final boolean DISPLAY_DEBUG = false;

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

        if(DISPLAY_DEBUG) {
            System.out.println(DEBUG_TAG + str);
        }

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
     * The logs are info logs.
     * @param description the description message that will be used for the logs
     * @param block the block to be executed
     * @param <T> the return type of the block
     * @return the return value of the block
     * @throws BlockFailedException if the block throws an exception
     */
    public <T> T withDebugLogs(String message, BlockWithReturn<T> block) throws BlockFailedException {
        return withDebugLogs(message, message, block);
    }

    public <T> T withDebugLogs(String message, String failMessage, BlockWithReturn<T> block) throws BlockFailedException {
        return withLogging(this::d, message, failMessage, block);
    }

    public void withDebugLogs(String message, Block block) throws BlockFailedException {
        withDebugLogs(message, message, block);
    }

    public void withDebugLogs(String message, String failMessage, Block block) throws BlockFailedException {
        withLogging(this::d, message, failMessage, block);
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
    public <T> T withInfoLogs(String message, BlockWithReturn<T> block) throws BlockFailedException {
        return withInfoLogs(message, message, block);
    }

    public <T> T withInfoLogs(String message, String failMessage, BlockWithReturn<T> block) throws BlockFailedException {
        return withLogging(this::i, message, failMessage, block);
    }

    public void withInfoLogs(String message, Block block) throws BlockFailedException {
        withInfoLogs(message, message, block);
    }

    public void withInfoLogs(String message, String failMessage, Block block) throws BlockFailedException {
        withLogging(this::i, message, failMessage, block);
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
    public <T> T withWarningLogs(String message, BlockWithReturn<T> block) throws BlockFailedException {
        return withWarningLogs(message, message, block);
    }

    public <T> T withWarningLogs(String message, String failMessage, BlockWithReturn<T> block) throws BlockFailedException {
        return withLogging(this::w, message, failMessage, block);
    }

    public void withWarningLogs(String message, Block block) throws BlockFailedException {
        withWarningLogs(message, message, block);
    }

    public void withWarningLogs(String message, String failMessage, Block block) throws BlockFailedException {
        withLogging(this::w, message, failMessage, block);
    }

    private void withLogging(Consumer<String> method, String message, String failMessage, Block block) throws BlockFailedException {
        method.accept("Starting " + message);

        try {
            block.run();
            method.accept("Completed " + message);
        }
        catch (Exception e) {
            e("Failed " + failMessage);
            throw new BlockFailedException(e.getMessage());
        }
    }


    private <T> T withLogging(Consumer<String> method, String message, String failMessage, BlockWithReturn<T> block) throws BlockFailedException {
        method.accept("Starting " + message);

        T res = null;

        try {
            res = block.run();
            method.accept("Completed " + message);
        }
        catch(Exception e) {
            e("Failed " + failMessage);
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

