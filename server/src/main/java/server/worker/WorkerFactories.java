package server.worker;

import server.processingstrategy.EchoProcessingStrategy;
import server.processingstrategy.PrimeDecomposeProcessingStrategy;
import server.processingstrategy.TimestampProcessingStrategy;

public class WorkerFactories {

    private WorkerFactories() {
        throw new AssertionError();
    }

    public static WorkerFactory timestampWorkerFactory() {
        return new WorkerFactory(new TimestampProcessingStrategy());
    }

    public static WorkerFactory echoWorkerFactory() {
        return new WorkerFactory(new EchoProcessingStrategy());
    }

    public static WorkerFactory primeDecomposeWorkerFactory() {
        return new WorkerFactory(new PrimeDecomposeProcessingStrategy());
    }

}
