package server.encoder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StreamEncoder {

    public static void writeString(DataOutputStream out, String msg) throws IOException {
        out.writeInt(msg.length());
        out.writeBytes(msg);
        out.flush();
    }

    public static String readString(DataInputStream in) throws IOException {
        int msgLength = in.readInt();
        byte[] buf = new byte[msgLength];
        in.readFully(buf);
        return new String(buf);
    }

}
