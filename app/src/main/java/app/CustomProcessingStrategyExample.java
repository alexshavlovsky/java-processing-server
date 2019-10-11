package app;

import client.PingClient;
import server.ProcessingServer;
import server.processingstrategy.ProcessingException;
import server.worker.WorkerFactory;

import java.net.InetAddress;

public class CustomProcessingStrategyExample {

    public static void main(String[] args) {

        int PORT = 8081;

        ProcessingServer server = new ProcessingServer("Math Square Server", PORT, new WorkerFactory(request -> {
            try {
                int i = Integer.parseInt(request);
                return String.valueOf(i * i);
            } catch (NumberFormatException e) {
                throw new ProcessingException("Wrong argument: integer number expected", 1);
            }
        }));

        server.start();

        PingClient client = new PingClient("Math Square Client", InetAddress.getLoopbackAddress(), PORT);

        try {
            System.out.println(client.sendPing("7"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        server.interrupt();

    }

}
