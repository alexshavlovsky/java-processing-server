package client;

import server.processingstrategy.ProcessingException;

import java.io.IOException;

public interface IPingClient {
    String sendPing(String message) throws IOException, ProcessingException;
}
