package client;

import core.ProcessingException;

import java.io.IOException;

interface IPingClient {
    String sendPing(String message) throws IOException, ProcessingException;
}
