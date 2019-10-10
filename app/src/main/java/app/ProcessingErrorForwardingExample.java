package app;

import client.PingClient;
import server.ProcessingServer;
import server.worker.WorkerFactories;

import java.net.InetAddress;

public class ProcessingErrorForwardingExample {
    public static void main(String[] args) throws InterruptedException {
        ProcessingServer server = new ProcessingServer("Math Server", 8080, WorkerFactories.mathSquareWorkerFactory());
        server.start();

        PingClient client = new PingClient("Math Client", InetAddress.getLoopbackAddress(), 8080, 5000);
        try {
            System.out.println(client.sendPing("x"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        server.interrupt();
        server.join();
    }
}
