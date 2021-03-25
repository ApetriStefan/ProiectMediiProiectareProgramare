package intrusii.client.Service;

import intrusii.client.TCP.TcpClient;
import intrusii.common.Message;
import intrusii.common.SocketClientService;
import intrusii.common.SocketClientService;

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
        return genericFunction(SocketClientService.ADD_CLIENT, client);
    }

    @Override
    public Future<String> deleteClient(String id) {
        return genericFunction(SocketClientService.DELETE_CLIENT, id);
    }

    @Override
    public Future<String> updateClient(String client) {
        return genericFunction(SocketClientService.UPDATE_CLIENT, client);
    }

    @Override
    public Future<String> getAllClients() {
        return genericFunction(SocketClientService.GET_ALL_CLIENTS, null);
    }

    @Override
    public Future<String> filterClientsByName(String name) {
        return genericFunction(SocketClientService.FILTER_CLIENTS_BY_NAME, name);
    }

    @Override
    public Future<String> filterClientsByCnp(String cnp) {
        return genericFunction(SocketClientService.FILTER_CLIENTS_BY_CNP, cnp);
    }

    public Future<String> genericFunction(String command, String parameter){
        return executorService.submit( () -> {
            Message request = new Message(command, parameter);
            Message response = tcpClient.sendAndReceive(request);

            return response.getBody();
        });
    }
}
