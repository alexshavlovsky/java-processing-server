package core;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProcessingDataInputStream extends DataInputStream {

    public ProcessingDataInputStream(InputStream in) {
        super(in);
    }

    public ClientStatus readClientStatus() throws IOException {
        return new ClientStatus(super.readInt());
    }

    public ProcessingException readProcessingException() throws IOException {
        int exitCode = super.readInt();
        if (exitCode == 0) return null;
        return new ProcessingException(this.readString(), exitCode);
    }

    public String readString() throws IOException {
        int msgLength = super.readInt();
        byte[] buf = new byte[msgLength];
        super.readFully(buf);
        return new String(buf);
    }

}
