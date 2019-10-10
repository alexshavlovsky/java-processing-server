package server;

import org.apache.log4j.Logger;
import server.worker.WorkerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProcessingServer extends Thread {

    private final Logger logger;
    private final int port;
    private final int socketTimeout;
    private final int backlog;
    private final int processingPoolSize;
    private final WorkerFactory workerFactory;
    private int messagesAccepted = 0;
    private int messagesProcessed = 0;
    private int clientRetriesCount = 0;

    public ProcessingServer(String serverThreadName, int port, WorkerFactory workerFactory, int clientSocketTimeout, int processingPoolSize, int backlog) {
        this.logger = Logger.getLogger(ProcessingServer.class + ":" + serverThreadName);
        this.setName(serverThreadName);
        this.port = port;
        this.socketTimeout = clientSocketTimeout;
        this.backlog = backlog;
        this.processingPoolSize = processingPoolSize;
        this.workerFactory = workerFactory;
    }

    public ProcessingServer(String serverThreadName, int port, WorkerFactory workerFactory) {
        this(serverThreadName, port, workerFactory, 1000, 10, 50);
    }

    public void run() {

        logger.info("Initialisation");

        ExecutorService processingPool = Executors.newFixedThreadPool(processingPoolSize);

        try (AutoCloseable close = processingPool::shutdown;
             ServerSocket serverSocket = new ServerSocket(port, backlog)) {

            logger.info("Listening on port " + serverSocket.getLocalPort());

            while (true) try {

                if (messagesAccepted % 1000 == 0 && messagesAccepted > 0)
                    logger.info("Clients accepted/processed/retries: " + messagesAccepted + "/" + messagesProcessed + "/" + clientRetriesCount);

                serverSocket.setSoTimeout(1000);
                Socket client = serverSocket.accept();
                messagesAccepted++;

                if (logger.isDebugEnabled())
                    logger.debug("Accepted connection from " + client.getInetAddress().getHostAddress());

                client.setSoTimeout(socketTimeout);
                Future<ClientStatus> future = processingPool.submit(workerFactory.createWorker(client));

                try {
                    ClientStatus clientStatus = future.get();
                    clientRetriesCount += clientStatus.getRetryConnectionCount();
                    messagesProcessed++;
                } catch (ExecutionException e) {
                    logger.error("Worker execution exception", e.getCause());
                } catch (InterruptedException e) {
                    break;
                }

            } catch (SocketTimeoutException e) {
                if (this.isInterrupted()) break;
                logger.debug("Server is alive");
            } catch (IOException e) {
                logger.error("Main loop exception", e);
                break;
            }

            logger.info("Shutting down");

        } catch (Exception e) {
            logger.error("Initialisation exception", e);
        }

        logger.info("Terminated");

    }

    public int getMessagesAccepted() {
        return messagesAccepted;
    }

    public int getMessagesProcessed() {
        return messagesProcessed;
    }

    public int getClientRetriesCount() {
        return clientRetriesCount;
    }

}
