package server.worker;

import server.processingstrategy.MathSquareProcessingStrategy;
import server.processingstrategy.TimestampProcessingStrategy;

public class WorkerFactories {

    private WorkerFactories() {
        throw new AssertionError();
    }

    public static WorkerFactory timestampWorkerFactory() {
        return new WorkerFactory(new TimestampProcessingStrategy());
    }

    public static WorkerFactory mathSquareWorkerFactory() {
        return new WorkerFactory(new MathSquareProcessingStrategy());
    }

}
