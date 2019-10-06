package server;

import java.net.Socket;

interface IWorker {
    Runnable newInstance(Socket socket);
}
