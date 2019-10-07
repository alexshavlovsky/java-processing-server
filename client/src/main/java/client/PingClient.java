package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class PingClient implements IPingClient {

    final private InetAddress address;
    final private int port;
    final private int readTimeout;

    public PingClient(InetAddress address, int port, int readTimeout) {
        this.address = address;
        this.port = port;
        this.readTimeout = readTimeout;
    }

    @Override
    public String sendPing(String message) {
        try (Socket socket = new Socket(address, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(message);
            socket.setSoTimeout(readTimeout);
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
