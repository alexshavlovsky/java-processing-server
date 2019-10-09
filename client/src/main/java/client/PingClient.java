package client;

import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import static server.encoder.StreamEncoder.readString;
import static server.encoder.StreamEncoder.writeString;

public class PingClient implements IPingClient {

    final private Logger logger;

    final private InetAddress address;
    final private int port;
    final private int readTimeout;

    public PingClient(String clientName, InetAddress address, int port, int readTimeout) {
        this.logger = Logger.getLogger(PingClient.class + ":" + clientName);
        this.address = address;
        this.port = port;
        this.readTimeout = readTimeout;
    }

    @Override
    public String sendPing(String msg) {
        if (logger.isDebugEnabled()) logger.debug("Connecting to " + address.getHostAddress() + ":" + port);
        while (true)
            try (Socket socket = new Socket(address, port);
                 DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                if (logger.isDebugEnabled()) logger.debug("Writing to socket " + msg.length() + " bytes");
                writeString(out, msg);
                socket.setSoTimeout(readTimeout);
                if (logger.isDebugEnabled()) logger.debug("Reading from socket");
                return readString(in);
            } catch (ConnectException e) {
                // reconnect
            } catch (IOException e) {
                logger.error("Exception", e);
                return null;
            }
    }

}
