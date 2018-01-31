package mqtt.utilities.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/**
 * Created by harry on 30/01/2018.
 */

class FileLogger implements BaseLogger {
    private Optional<PrintWriter> errorFile;
    private Optional<PrintWriter> debugFile;
    private Optional<PrintWriter> infoFile;
    private Optional<PrintWriter> warnFile;

    private ArrayList<Optional<PrintWriter>> files = new ArrayList<>();

    FileLogger(String errorFileName, String debugFileName, String infoFileName, String warnFileName) {
        errorFile = openFile(errorFileName);
        debugFile = openFile(debugFileName);
        infoFile = openFile(infoFileName);
        warnFile = openFile(warnFileName);

        files.add(errorFile);
        files.add(debugFile);
        files.add(infoFile);
        files.add(warnFile);
    }

    /**
     * Writes a log in the error file.
     * @param msg the log to be written
     * @return true if the log has been written successfully, false otherwise
     */
    @Override
    public boolean e(String msg) {
        return writeLog(errorFile, msg);
    }

    /**
     * Writes a log in the debug file.
     * @param msg the log to be written
     * @return true if the log has been written successfully, false otherwise
     */
    @Override
    public boolean d(String msg) {
        return writeLog(debugFile, msg);
    }

    /**
     * Writes a log in the info file.
     * @param msg the log to be written
     * @return true if the log has been written successfully, false otherwise
     */
    @Override
    public boolean i(String msg) {
        return writeLog(infoFile, msg);
    }

    /**
     * Writes a log in the warning file.
     * @param msg the log to be written
     * @return true if the log has been written successfully, false otherwise
     */
    @Override
    public boolean w(String msg) {
        return writeLog(warnFile, msg);
    }

    /**
     * Returns true if all streams closed successfully, false if there was an exception in at least one of the streams
     * @return true if all streams closed successfully, false if there was an exception in at least one of the streams
     */
    @Override
    public boolean close() {
        boolean raisedException = false;

        for(Optional<PrintWriter> file : files) {
            if(file.isPresent()) {
                raisedException |= IgnoreException.execute(() -> file.get().close());
            }
        }

        return !raisedException;
    }

    private boolean writeLog(Optional<PrintWriter> file, String msg) {
        if(file.isPresent()) {
            file.get().println(addTimeStamp(msg));
            return file.get().checkError(); // make sure that this clears the flag
        }

        return false;
    }

    private Optional<PrintWriter> openFile(String fileName) {
        if(fileName == null) {
            return Optional.empty();
        }

        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileWriter(fileName, true));
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(writer);
    }

    private String addTimeStamp(String msg) {
        return msg + " -- " + new Date().toString();
    }

    static class FileLoggerBuilder {
        private String errorFile;
        private String debugFile;
        private String infoFile;
        private String warnFile;

        FileLoggerBuilder setErrorFile(String errorFile) {
            this.errorFile = errorFile;
            return this;
        }

        FileLoggerBuilder setDebugFile(String debugFile) {
            this.debugFile = debugFile;
            return this;
        }

        FileLoggerBuilder setInfoFile(String infoFile) {
            this.infoFile = infoFile;
            return this;
        }

        FileLoggerBuilder setWarnFile(String warnFile) {
            this.warnFile = warnFile;
            return this;
        }

        FileLogger build() {
            return new FileLogger(errorFile, debugFile, infoFile, warnFile);
        }
    }

}
