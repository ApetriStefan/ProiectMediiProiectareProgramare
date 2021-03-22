package intrusii.client.Controller;

import intrusii.client.TCP.TcpClient;
import intrusii.common.Message;
import intrusii.common.SocketController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class SocketClientController implements SocketController {
    private ExecutorService executorService;
    private TcpClient tcpClient;

    public SocketClientController(ExecutorService executorService, TcpClient tcpClient){
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public Future<String> addClient(String client) {
        return executorService.submit( () -> {
            Message request = new Message(SocketController.ADD_CLIENT, client);
            Message response = tcpClient.sendAndReceive(request);

            String result = response.getBody();

            return result;
        });
    }

    @Override
    public Future<String> deleteClient(String id) {
        return null;
    }

    @Override
    public Future<String> updateClient(String client) {
        return null;
    }

    @Override
    public Future<String> getAllClients() {
        return null;
    }

    @Override
    public Future<String> filterClientsByName(String name) {
        return null;
    }

    @Override
    public Future<String> filterClientsByCnp(String cnp) {
        return null;
    }
}
