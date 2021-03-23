package intrusii.client.TCP;

import intrusii.common.Message;
import intrusii.common.SocketController;
import intrusii.common.SocketException;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class TcpClient {
    private ExecutorService executorService;

    public TcpClient(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public Message sendAndReceive(Message request) {
        try (var socket = new Socket(SocketController.HOST, SocketController.PORT);
             var is = socket.getInputStream();
             var os = socket.getOutputStream()) {

            System.out.println("******************************");
            System.out.println("Sending request: " + request);
            request.writeTo(os);
            System.out.println("Request sent");

            Message response = new Message();
            response.readFrom(is);
            System.out.println("Received response: " + response);
            System.out.println("******************************");

            return response;

        } catch (IOException e) {
            e.printStackTrace();
            throw new SocketException("Exception in send and receive", e);
        }

    }
}
