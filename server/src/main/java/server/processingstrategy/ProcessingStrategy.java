package server.processingstrategy;

@FunctionalInterface
public interface ProcessingStrategy {
    String process(String request) throws ProcessingException;
}
