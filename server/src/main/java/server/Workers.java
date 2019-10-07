package server;

public class Workers {

    private Workers() {
        throw new AssertionError();
    }

    public static Worker timestampWorker() {
        return new Worker(new TimestampProcessingStrategy());
    }

    public static Worker mathSquareWorker() {
        return new Worker(new MathSquareProcessingStrategy());
    }

}
