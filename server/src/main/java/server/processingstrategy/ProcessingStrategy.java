package server.processingstrategy;

import core.ProcessingException;

@FunctionalInterface
public interface ProcessingStrategy {
    String process(String request) throws ProcessingException;
}
