package server.worker;

import java.net.Socket;
import java.util.concurrent.Callable;

interface IWorkerFactory {
    Callable<Integer> createWorker(Socket client);
}
