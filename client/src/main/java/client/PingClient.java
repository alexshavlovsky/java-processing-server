package client;

import core.ClientStatus;
import core.ProcessingDataInputStream;
import core.ProcessingDataOutputStream;
import core.ProcessingException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

public class PingClient implements IPingClient {

    final private Logger logger;

    final private InetAddress address;
    final private int port;
    final private int readTimeout;
    final private int RECONNECT_RETRIES = 3;
    private int retryConnectionCount = 0;

    public PingClient(String clientName, InetAddress address, int port, int readTimeout) {
        this.logger = Logger.getLogger(PingClient.class + ":" + clientName);
        this.address = address;
        this.port = port;
        this.readTimeout = readTimeout;
    }

    public PingClient(String clientName, InetAddress address, int port) {
        this(clientName, address, port, 5000);
    }

    @Override
    public String sendPing(String msg) throws IOException, ProcessingException {
        if (logger.isDebugEnabled()) logger.debug("Connecting to " + address.getHostAddress() + ":" + port);
        while (true)
            try (Socket socket = new Socket(address, port);
                 ProcessingDataInputStream in = new ProcessingDataInputStream(socket.getInputStream());
                 ProcessingDataOutputStream out = new ProcessingDataOutputStream(socket.getOutputStream())) {
                if (logger.isDebugEnabled()) logger.debug("Writing to socket " + msg.length() + " bytes");
                out.writeString(msg);
                out.writeClientStatus(new ClientStatus(retryConnectionCount));
                socket.setSoTimeout(readTimeout);

                logger.debug("Read processing status");
                ProcessingException exception = in.readProcessingException();

                if (exception != null) {
                    logger.error("Processing error received");
                    throw exception;
                }

                logger.debug("Read payload");
                return in.readString();
            } catch (ConnectException e) {
                retryConnectionCount++;
                if (retryConnectionCount > RECONNECT_RETRIES) {
                    logger.error("Connection refused");
                    throw e;
                }
                if (logger.isDebugEnabled()) logger.debug("Connection retry " + retryConnectionCount);
            }
    }

}
