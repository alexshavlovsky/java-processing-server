package server.processingstrategy;

public class ProcessingException extends Exception {

    private final int exitCode;

    ProcessingException(Exception e, int exitCode) {
        super(e);
        this.exitCode = exitCode;
    }

    public ProcessingException(String message, int exitCode) {
        super(message);
        this.exitCode = exitCode;
    }

    public int getExitCode() {
        return exitCode;
    }

}
