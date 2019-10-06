package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Worker implements IWorker {

    final private IProcessingStrategy pongStrategy;

    Worker(IProcessingStrategy pongStrategy) {
        this.pongStrategy = pongStrategy;
    }

    @Override
    public Runnable newInstance(Socket socket) {
        return () -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                String input = in.readLine();
                String output = pongStrategy.process(input);
                out.println(output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

}
