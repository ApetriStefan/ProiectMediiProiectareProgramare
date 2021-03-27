package intrusii.client.TCP;

import intrusii.common.Message;
import intrusii.common.SocketException;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class TcpClient {
    private final int PORT;
    private final String HOST;

    public TcpClient(String HOST, int PORT) {
        this.PORT = PORT;
        this.HOST = HOST;
    }

    public Message sendAndReceive(Message request) {
        try (var socket = new Socket(HOST, PORT);
             var is = socket.getInputStream();
             var os = socket.getOutputStream()) {

            System.out.println("\n******************************");
            System.out.println("Sending request: " + request);
            request.writeTo(os);
            System.out.println("Request sent");

            Message response = new Message();
            response.readFrom(is);
            System.out.println("Received response: " + response);
            Thread.sleep(2000);
            return response;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new SocketException("Exception in send and receive", e);
        }

    }
}
