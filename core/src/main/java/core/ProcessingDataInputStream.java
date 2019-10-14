package core;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProcessingDataInputStream extends DataInputStream {

    public ProcessingDataInputStream(InputStream in) {
        super(in);
    }

    private String readString() throws IOException {
        int msgLength = readInt();
        byte[] buf = new byte[msgLength];
        readFully(buf);
        return new String(buf);
    }

    private ClientStatus readClientStatus() throws IOException {
        return new ClientStatus(readInt());
    }

    public ServerRequest readServerRequest() throws IOException {
        ClientStatus clientStatus = readClientStatus();
        String payload = readString();
        return new ServerRequest(clientStatus, payload);
    }

    private ProcessingException readProcessingException() throws IOException {
        int exitCode = readInt();
        if (exitCode == 0) return null;
        return new ProcessingException(readString(), exitCode);
    }

    public ServerResponse readServerResponse() throws IOException {
        ProcessingException exception = readProcessingException();
        if (exception != null) return new ServerResponse(null, exception);
        return new ServerResponse(readString(), null);
    }

}
