package intrusii.client.Service;

import intrusii.client.TCP.TcpClient;
import intrusii.common.Message;
import intrusii.common.SocketClientService;
import intrusii.common.SocketController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class SocketClientServiceImpl implements SocketClientService
{
    private ExecutorService executorService;
    private TcpClient tcpClient;

    public SocketClientServiceImpl(ExecutorService executorService, TcpClient tcpClient){
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public Future<String> addClient(String client) {
        return genericFunction(SocketController.ADD_CLIENT, client);
    }

    @Override
    public Future<String> deleteClient(String id) {
        return genericFunction(SocketController.DELETE_CLIENT, id);
    }

    @Override
    public Future<String> updateClient(String client) {
        return genericFunction(SocketController.UPDATE_CLIENT, client);
    }

    @Override
    public Future<String> getAllClients() {
        return genericFunction(SocketController.GET_ALL_CLIENTS, null);
    }

    @Override
    public Future<String> filterClientsByName(String name) {
        return genericFunction(SocketController.FILTER_CLIENTS_BY_NAME, name);
    }

    @Override
    public Future<String> filterClientsByCnp(String cnp) {
        return genericFunction(SocketController.FILTER_CLIENTS_BY_CNP, cnp);
    }

    public Future<String> genericFunction(String command, String parameter){
        return executorService.submit( () -> {
            Message request = new Message(command, parameter);
            Message response = tcpClient.sendAndReceive(request);

            return response.getBody();
        });
    }
}
