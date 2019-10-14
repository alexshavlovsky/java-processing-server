package core;

public class ServerResponse {

    private final String responsePayload;
    private final ProcessingException exception;

    public ServerResponse(String responsePayload, ProcessingException exception) {
        this.responsePayload = responsePayload;
        this.exception = exception;
    }

    public String getResponsePayload() {
        return responsePayload;
    }

    public ProcessingException getException() {
        return exception;
    }

}
