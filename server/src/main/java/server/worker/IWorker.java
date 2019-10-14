package server.worker;

import core.ClientStatus;

import java.util.concurrent.Callable;

interface IWorker extends Callable<ClientStatus> {
}
