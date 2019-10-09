package server.worker;

import server.processingstrategy.ProcessingStrategy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

import static server.encoder.StreamEncoder.readString;
import static server.encoder.StreamEncoder.writeString;

public class WorkerFactory implements IWorkerFactory {

    final private ProcessingStrategy pongStrategy;

    WorkerFactory(ProcessingStrategy pongStrategy) {
        this.pongStrategy = pongStrategy;
    }

    @Override
    public Callable<Integer> createWorker(Socket client) {
        return () -> {
            try (DataInputStream in = new DataInputStream(client.getInputStream());
                 DataOutputStream out = new DataOutputStream(client.getOutputStream())) {
                String input = readString(in);
                String output = pongStrategy.process(input);
                writeString(out, output);
                return 0;
            }
        };
    }

}
