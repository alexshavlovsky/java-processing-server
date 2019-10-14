package server.worker;

import core.ClientStatus;
import core.ProcessingException;
import server.processingstrategy.ProcessingStrategy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static core.StreamEncoder.*;

class Worker implements IWorker {

    private final ProcessingStrategy processingStrategy;
    private final Socket client;

    Worker(ProcessingStrategy processingStrategy, Socket client) {
        this.processingStrategy = processingStrategy;
        this.client = client;
    }

    @Override
    public ClientStatus call() throws IOException {
        try (DataInputStream in = new DataInputStream(client.getInputStream());
             DataOutputStream out = new DataOutputStream(client.getOutputStream())) {

            String input = readString(in);
            ClientStatus clientStatus = readClientStatus(in);

            try {
                String output = processingStrategy.process(input);
                out.writeInt(0);
                writeString(out, output);
            } catch (ProcessingException e) {
                writeProcessingException(out, e);
            }

            return clientStatus;
        }
    }

}
