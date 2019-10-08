package server.worker;

import java.net.Socket;

interface IWorkerFactory {
    Runnable createWorker(Socket client);
}
