package core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProcessingDataOutputStream extends DataOutputStream {

    public ProcessingDataOutputStream(OutputStream out) {
        super(out);
    }

    private void writeString(String msg) throws IOException {
        writeInt(msg.length());
        writeBytes(msg);
        flush();
    }

    private void writeClientStatus(ClientStatus msg) throws IOException {
        writeInt(msg.getRetryConnectionCount());
    }

    private void writeProcessingException(ProcessingException e) throws IOException {
        writeInt(e.getExitCode());
        writeString(e.getMessage());
    }

    public void writeServerRequest(ServerRequest serverRequest) throws IOException {
        writeClientStatus(serverRequest.getClientStatus());
        writeString(serverRequest.getRequestPayload());
    }

    public void writeServerResponse(ServerResponse serverResponse) throws IOException {
        if (serverResponse.getException() == null) {
            writeInt(0);
            writeString(serverResponse.getResponsePayload());
        } else {
            writeProcessingException(serverResponse.getException());
        }
    }

}
