package app;

import client.PingClient;
import server.ProcessingServer;
import server.worker.WorkerFactories;

import java.net.InetAddress;

public class TwoServicesExample {
    public static void main(String[] args) {

        ProcessingServer primeDecomposeServer = new ProcessingServer("Prime Decompose Server", 8080, WorkerFactories.primeDecomposeWorkerFactory());
        ProcessingServer timestampServer = new ProcessingServer("Timestamp Server", 8081, WorkerFactories.timestampWorkerFactory());

        primeDecomposeServer.start();
        timestampServer.start();

        PingClient primeDecomposeClient = new PingClient("Prime Decompose Client", InetAddress.getLoopbackAddress(), 8080);
        PingClient timestampClient = new PingClient("Timestamp Client", InetAddress.getLoopbackAddress(), 8081);

        for (int i = 2; i < 1000; i++)
            try {
                String primeDecomposeResult = i + " = " + primeDecomposeClient.sendPing(String.valueOf(i));
                String timestampResult = timestampClient.sendPing(primeDecomposeResult);
                System.out.println(timestampResult);
            } catch (Exception e) {
                e.printStackTrace();
            }

        primeDecomposeServer.interrupt();
        timestampServer.interrupt();

    }
}
