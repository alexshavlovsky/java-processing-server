package server.processingstrategy;

import java.util.Date;

public class TimestampProcessingStrategy implements ProcessingStrategy {

    @Override
    public String process(String request) {
        return new Date() + ": " + request;
    }

}
