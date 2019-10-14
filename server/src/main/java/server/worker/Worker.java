package server.worker;

import core.ClientStatus;
import core.ProcessingDataInputStream;
import core.ProcessingDataOutputStream;
import core.ProcessingException;
import server.processingstrategy.ProcessingStrategy;

import java.io.IOException;
import java.net.Socket;

class Worker implements IWorker {

    private final ProcessingStrategy processingStrategy;
    private final Socket client;

    Worker(ProcessingStrategy processingStrategy, Socket client) {
        this.processingStrategy = processingStrategy;
        this.client = client;
    }

    @Override
    public ClientStatus call() throws IOException {
        try (ProcessingDataInputStream in = new ProcessingDataInputStream(client.getInputStream());
             ProcessingDataOutputStream out = new ProcessingDataOutputStream(client.getOutputStream())) {

            String input = in.readString();
            ClientStatus clientStatus = in.readClientStatus();

            try {
                String output = processingStrategy.process(input);
                out.writeInt(0);
                out.writeString(output);
            } catch (ProcessingException e) {
                out.writeProcessingException(e);
            }

            return clientStatus;
        }
    }

}
