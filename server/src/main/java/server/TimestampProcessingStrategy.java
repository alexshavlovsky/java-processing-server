package server;

import java.util.Date;

class TimestampProcessingStrategy implements IProcessingStrategy {

    @Override
    public String process(String request) {
        return new Date() + ": " + request;
    }

}
