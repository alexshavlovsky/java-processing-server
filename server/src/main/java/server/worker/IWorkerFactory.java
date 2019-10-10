package server.worker;

import java.net.Socket;

interface IWorkerFactory {
    IWorker createWorker(Socket client);
}
