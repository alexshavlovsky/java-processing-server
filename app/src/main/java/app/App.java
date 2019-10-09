package app;

import client.PingClient;
import server.ProcessingServer;
import server.worker.WorkerFactories;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) throws InterruptedException {
        int MESSAGES_COUNT = 50;
        ProcessingServer server = new ProcessingServer("Math Square Server", 8080, 1000, 2, WorkerFactories.mathSquareWorkerFactory());

        server.start();

        long t0 = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i = 1; i <= MESSAGES_COUNT; i++) {
            String clientName = "" + i;
            executor.submit(() -> {
                PingClient client = new PingClient(InetAddress.getLoopbackAddress(), 8080, 1000);
                String pong = client.sendPing(clientName);
//                System.out.print(clientName+"->");
//                if (pong == null) System.out.println("Error");
//                else System.out.println(pong);
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        server.interrupt();
        System.out.println("wait server termination");
        server.join();
        System.out.println(MESSAGES_COUNT * 1000 / (System.currentTimeMillis() - t0) + " msg/s");

    }

}