package mqtt.utilities.logger;

import java.util.function.Consumer;

/**
 * Created by harry on 30/01/2018.
 */

public class Logger implements BaseLogger {
    private final String tag;

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

    @Override
    public boolean e(String msg) {
        return logger.e(addTag(msg));
    }

    @Override
    public boolean d(String msg) {
        return logger.d(addTag(msg));
    }

    @Override
    public boolean i(String msg) {
        return logger.i(addTag(msg));
    }

    @Override
    public boolean w(String msg) {
        return logger.w(addTag(msg));
    }


    public <T> T withErrorLogs(String description, BlockWithReturn<T> block) throws BlockFailedException {
        return withLogging(this::e, description, block);
    }

    public <T> T withDebugLogs(String description, BlockWithReturn<T> block) throws BlockFailedException {
        return withLogging(this::d, description, block);
    }

    public <T> T withInfoLogs(String description, BlockWithReturn<T> block) throws BlockFailedException {
        return withLogging(this::i, description, block);
    }

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

    @Override
    public boolean close() {
        return logger.close();
    }

    private String addTag(String msg) {
        return tag + " -- " + msg;
    }
}

