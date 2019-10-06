package server;

@FunctionalInterface
interface IProcessingStrategy {
    String process(String request);
}
