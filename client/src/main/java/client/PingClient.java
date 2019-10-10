package client;

import org.apache.log4j.Logger;
import server.ClientStatus;
import server.processingstrategy.ProcessingException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import static server.StreamEncoder.*;

public class PingClient implements IPingClient {

    final private Logger logger;

    final private InetAddress address;
    final private int port;
    final private int readTimeout;
    private int retryConnectionCount = 0;

    public PingClient(String clientName, InetAddress address, int port, int readTimeout) {
        this.logger = Logger.getLogger(PingClient.class + ":" + clientName);
        this.address = address;
        this.port = port;
        this.readTimeout = readTimeout;
    }

    @Override
    public String sendPing(String msg) throws IOException, ProcessingException {
        if (logger.isDebugEnabled()) logger.debug("Connecting to " + address.getHostAddress() + ":" + port);
        while (true)
            try (Socket socket = new Socket(address, port);
                 DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                if (logger.isDebugEnabled()) logger.debug("Writing to socket " + msg.length() + " bytes");
                writeString(out, msg);
                writeClientStatus(out, new ClientStatus(retryConnectionCount));
                socket.setSoTimeout(readTimeout);

                logger.debug("Read processing status");
                ProcessingException exception = readProcessingException(in);

                if (exception != null) {
                    logger.error("Processing error received");
                    throw exception;
                }

                logger.debug("Read payload");
                return readString(in);
            } catch (ConnectException e) {
                retryConnectionCount++;
            }
    }

}
