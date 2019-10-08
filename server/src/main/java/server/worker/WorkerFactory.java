package server.worker;

import server.processingstrategy.ProcessingStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WorkerFactory implements IWorkerFactory {

    final private ProcessingStrategy pongStrategy;

    WorkerFactory(ProcessingStrategy pongStrategy) {
        this.pongStrategy = pongStrategy;
    }

    @Override
    public Runnable createWorker(Socket client) {
        return () -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                 PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
                String input = in.readLine();
                String output = pongStrategy.process(input);
                out.println(output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

}
