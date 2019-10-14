package core;

public class ServerRequest {

    private final ClientStatus clientStatus;
    private final String requestPayload;

    public ServerRequest(ClientStatus clientStatus, String requestPayload) {
        this.clientStatus = clientStatus;
        this.requestPayload = requestPayload;
    }

    public ClientStatus getClientStatus() {
        return clientStatus;
    }

    public String getRequestPayload() {
        return requestPayload;
    }

}
