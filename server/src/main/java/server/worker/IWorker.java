package server.worker;

import server.ClientStatus;

import java.util.concurrent.Callable;

interface IWorker extends Callable<ClientStatus> {
}
