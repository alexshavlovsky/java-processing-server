package server;

public class ClientStatus {

    private final int RetryConnectionCount;

    public ClientStatus(int retryConnectionCount) {
        RetryConnectionCount = retryConnectionCount;
    }

    int getRetryConnectionCount() {
        return RetryConnectionCount;
    }

}
