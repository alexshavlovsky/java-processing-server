package core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProcessingDataOutputStream extends DataOutputStream {

    public ProcessingDataOutputStream(OutputStream out) {
        super(out);
    }

    public void writeClientStatus(ClientStatus msg) throws IOException {
        super.writeInt(msg.getRetryConnectionCount());
        super.flush();
    }

    public void writeString(String msg) throws IOException {
        super.writeInt(msg.length());
        super.writeBytes(msg);
        super.flush();
    }

    public void writeProcessingException(ProcessingException e) throws IOException {
        super.writeInt(e.getExitCode());
        this.writeString(e.getMessage());
    }

}
