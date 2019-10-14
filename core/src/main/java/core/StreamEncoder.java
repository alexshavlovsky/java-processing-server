package core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StreamEncoder {

    public static void writeClientStatus(DataOutputStream out, ClientStatus msg) throws IOException {
        out.writeInt(msg.getRetryConnectionCount());
        out.flush();
    }

    public static void writeString(DataOutputStream out, String msg) throws IOException {
        out.writeInt(msg.length());
        out.writeBytes(msg);
        out.flush();
    }

    public static void writeProcessingException(DataOutputStream out, ProcessingException e) throws IOException {
        out.writeInt(e.getExitCode());
        writeString(out, e.getMessage());
    }

    public static ClientStatus readClientStatus(DataInputStream in) throws IOException {
        return new ClientStatus(in.readInt());
    }

    public static ProcessingException readProcessingException(DataInputStream in) throws IOException {
        int exitCode = in.readInt();
        if (exitCode == 0) return null;
        return new ProcessingException(readString(in), exitCode);
    }

    public static String readString(DataInputStream in) throws IOException {
        int msgLength = in.readInt();
        byte[] buf = new byte[msgLength];
        in.readFully(buf);
        return new String(buf);
    }

}
