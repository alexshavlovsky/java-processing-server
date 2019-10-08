package server;

import org.apache.log4j.Logger;
import server.worker.WorkerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessingServer extends Thread {

    private final static Logger logger = Logger.getLogger(ProcessingServer.class);
    private final int port;
    private final int socketTimeout;
    private final int processingPoolSize;
    private final WorkerFactory workerFactory;

    public ProcessingServer(String serverThreadName, int port, int clientSocketTimeout, int processingPoolSize, WorkerFactory workerFactory) {
        this.setName(serverThreadName);
        this.port = port;
        this.socketTimeout = clientSocketTimeout;
        this.processingPoolSize = processingPoolSize;
        this.workerFactory = workerFactory;
    }

    public void run() {

        logger.info(this.getName() + " initialisation...");

        ExecutorService processingPool = Executors.newFixedThreadPool(processingPoolSize);

        try (AutoCloseable close = processingPool::shutdown;
             ServerSocket serverSocket = new ServerSocket(port)) {

            logger.info(this.getName() + " is running on port " + serverSocket.getLocalPort());

            while (true) {
                try {

                    serverSocket.setSoTimeout(500);
                    Socket client = serverSocket.accept();
//                    if (logger.isDebugEnabled())

                    client.setSoTimeout(socketTimeout);
                    processingPool.submit(workerFactory.createWorker(client));

                } catch (SocketTimeoutException e) {
                    if (this.isInterrupted()) break;
                } catch (IOException e) {
                    logger.error(this.getName() + " error", e);
                    break;
                }
            }

            logger.info(this.getName() + " shutting down...");

        } catch (Exception e) {
            logger.error(this.getName() + " error", e);
        }

        logger.info(this.getName() + " stopped");

    }

}
