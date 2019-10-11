package app;

import client.PingClient;
import server.ProcessingServer;
import server.worker.WorkerFactories;

import java.net.InetAddress;

public class ProcessingErrorForwardingExample {

    public static void main(String[] args) {

        int PORT = 8081;

        ProcessingServer server = new ProcessingServer("Prime Decompose Server", PORT, WorkerFactories.primeDecomposeWorkerFactory());
        server.start();

        PingClient client = new PingClient("Prime decompose Client", InetAddress.getLoopbackAddress(), PORT);

        try {
            System.out.println(client.sendPing("3.141"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        server.interrupt();

    }

}
