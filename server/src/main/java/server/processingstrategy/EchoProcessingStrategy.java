package server.processingstrategy;

public class EchoProcessingStrategy implements ProcessingStrategy {

    @Override
    public String process(String request) {
        return request;
    }

}
