package server.worker;

import core.*;
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

            ServerRequest serverRequest = in.readServerRequest();

            out.writeServerResponse(process(serverRequest.getRequestPayload()));

            return serverRequest.getClientStatus();
        }
    }

    private ServerResponse process(String requestPayload) {
        String responsePayload = null;
        ProcessingException processingException = null;
        try {
            responsePayload = processingStrategy.process(requestPayload);
        } catch (ProcessingException e) {
            processingException = e;
        }
        return new ServerResponse(responsePayload, processingException);
    }

}
