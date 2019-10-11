package app;

import client.PingClient;
import server.ProcessingServer;
import server.worker.WorkerFactories;

import java.net.InetAddress;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PrimeDecomposeBenchmark {

    public static void main(String[] args) throws InterruptedException {

        int MESSAGES_COUNT = 10000;
        int PORT = 8081;
        int CONCURRENT_CLIENTS = 100;

        ProcessingServer server = new ProcessingServer("Prime Decompose Server", PORT, WorkerFactories.primeDecomposeWorkerFactory());
        server.start();

        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_CLIENTS);

        long t0 = System.currentTimeMillis();

        for (int i = 0; i < MESSAGES_COUNT; i++) {
            String clientName = "Client-" + (i + 1);
            executor.submit(() -> {
                try {
                    int n = 1000 + new Random().nextInt(1000000);
                    new PingClient(clientName, InetAddress.getLoopbackAddress(), PORT).sendPing(String.valueOf(n));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(120, TimeUnit.SECONDS);

        server.interrupt();
        server.join();

        System.out.println(MESSAGES_COUNT * 1000 / (System.currentTimeMillis() - t0) + " msg/s");

    }

}
