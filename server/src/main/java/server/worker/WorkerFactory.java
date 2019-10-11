package server.worker;

import server.processingstrategy.ProcessingStrategy;

import java.net.Socket;

public class WorkerFactory implements IWorkerFactory {

    final private ProcessingStrategy pongStrategy;

    public WorkerFactory(ProcessingStrategy pongStrategy) {
        this.pongStrategy = pongStrategy;
    }

    @Override
    public IWorker createWorker(Socket client) {
        return new Worker(pongStrategy, client);
    }

}
