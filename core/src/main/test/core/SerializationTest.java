package core;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class SerializationTest {

    private ByteArrayOutputStream buffer;

    private ProcessingDataOutputStream getTestOutputStream() {
        buffer = new ByteArrayOutputStream();
        return new ProcessingDataOutputStream(new DataOutputStream(buffer));
    }

    private ProcessingDataInputStream getTestInputStream() {
        return new ProcessingDataInputStream(new DataInputStream(new ByteArrayInputStream(buffer.toByteArray())));
    }

    @Test
    public void ClientToServerPassThroughTest() throws IOException {

        // prepare test request
        int retries = 5;
        ClientStatus clientStatus = new ClientStatus(retries);
        String payload = "Request payload";
        ServerRequest request = new ServerRequest(clientStatus, payload);

        // serialization
        getTestOutputStream().writeServerRequest(request);

        // deserialization
        ServerRequest requestFromWire = getTestInputStream().readServerRequest();

        // assertions
        Assert.assertEquals(retries, requestFromWire.getClientStatus().getRetryConnectionCount());
        Assert.assertEquals(payload, requestFromWire.getRequestPayload());

    }

    @Test
    public void ServerToClientPassThroughTest() throws IOException {

        // prepare test response
        String payload = "Response payload";
        ServerResponse response = new ServerResponse(payload, null);

        // serialization
        getTestOutputStream().writeServerResponse(response);

        // deserialization
        ServerResponse responseFromWire = getTestInputStream().readServerResponse();

        // assertions
        Assert.assertEquals(payload, responseFromWire.getResponsePayload());
        Assert.assertNull(responseFromWire.getException());

    }

    @Test
    public void ServerToClientExceptionForwardingTest() throws IOException {

        // prepare test response
        int exitCode = 3;
        String message = "Exception message";
        ProcessingException exception = new ProcessingException(message, exitCode);
        ServerResponse response = new ServerResponse(null, exception);

        // serialization
        getTestOutputStream().writeServerResponse(response);

        // deserialization
        ServerResponse responseFromWire = getTestInputStream().readServerResponse();

        // assertions
        Assert.assertEquals(message, responseFromWire.getException().getMessage());
        Assert.assertEquals(exitCode, responseFromWire.getException().getExitCode());
        Assert.assertNull(responseFromWire.getResponsePayload());

    }

}
