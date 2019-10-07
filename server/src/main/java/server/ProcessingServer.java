package server;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessingServer extends Thread {

    private final static Logger logger = Logger.getLogger(ProcessingServer.class);
    private final int port;
    private final int socketTimeout;
    private final int processingPoolSize;
    private final IWorker workerFactory;

    public ProcessingServer(String serverThreadName, int port, int socketTimeout, int processingPoolSize, IWorker workerFactory) {
        this.setName(serverThreadName);
        this.port = port;
        this.socketTimeout = socketTimeout;
        this.processingPoolSize = processingPoolSize;
        this.workerFactory = workerFactory;
    }

    public void run() {
        logger.info(this.getName() + " initialisation...");
        ExecutorService processingPool = Executors.newFixedThreadPool(processingPoolSize);
        try (AutoCloseable close = processingPool::shutdown;
             ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setSoTimeout(socketTimeout);
            logger.info(this.getName() + " is running on port " + serverSocket.getLocalPort());
            while (true) {
                try {
                    processingPool.submit(workerFactory.newInstance(serverSocket.accept()));
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
