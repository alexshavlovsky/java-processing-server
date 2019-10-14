package core;

public class ClientStatus {

    private final int RetryConnectionCount;

    public ClientStatus(int retryConnectionCount) {
        RetryConnectionCount = retryConnectionCount;
    }

    public int getRetryConnectionCount() {
        return RetryConnectionCount;
    }

}
