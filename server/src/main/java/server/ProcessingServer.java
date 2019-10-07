package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessingServer extends Thread {

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
        System.out.println(this.getName() + " initialisation...");
        ExecutorService processingPool = Executors.newFixedThreadPool(processingPoolSize);
        try (AutoCloseable close = processingPool::shutdown;
             ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setSoTimeout(socketTimeout);
            System.out.println(this.getName() + " is running on port " + serverSocket.getLocalPort());
            while (true) {
                try {
                    processingPool.submit(workerFactory.newInstance(serverSocket.accept()));
                } catch (SocketTimeoutException e) {
                    if (this.isInterrupted()) break;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            System.out.println(this.getName() + " shutting down...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(this.getName() + " stopped");
    }

}
