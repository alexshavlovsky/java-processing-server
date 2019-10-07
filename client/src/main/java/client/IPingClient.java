package client;

import java.io.IOException;

public interface IPingClient {
    String sendPing(String message) throws IOException;
}
