package app;

import client.PingClient;
import server.ProcessingServer;
import server.worker.WorkerFactories;

import java.net.InetAddress;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BenchmarkApp {

    private static final Random RANDOM = new Random();

    private static String getRandomString(int targetStringLength) {
        int leftLimit = 97;
        int rightLimit = 122;
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (RANDOM.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }


    public static void main(String[] args) throws InterruptedException {

        int MESSAGES_COUNT = 10000;
        int MESSAGE_LENGTH = 20;
        int PORT = 8080;
        int SERVER_THREADS = 10;
        int CLIENT_THREADS = 100;

        ProcessingServer server = new ProcessingServer("Echo Server", PORT, WorkerFactories.echoWorkerFactory(), 1000, SERVER_THREADS, CLIENT_THREADS);

        server.start();

        ExecutorService executor = Executors.newFixedThreadPool(CLIENT_THREADS);
        for (int i = 0; i < MESSAGES_COUNT; i++) {
            String clientName = "Client-" + (i + 1);
            String msg = getRandomString(MESSAGE_LENGTH);
            executor.submit(() -> {
                String pong = null;
                try {
                    pong = new PingClient(clientName, InetAddress.getLoopbackAddress(), PORT, 5000).sendPing(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!msg.equals(pong)) System.out.println(clientName + " error");
            });
        }

        long t0 = System.currentTimeMillis();

        executor.shutdown();
        try {
            executor.awaitTermination(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        server.interrupt();
        server.join();
        System.out.println(MESSAGES_COUNT * 1000 / (System.currentTimeMillis() - t0) + " msg/s");

    }

}
