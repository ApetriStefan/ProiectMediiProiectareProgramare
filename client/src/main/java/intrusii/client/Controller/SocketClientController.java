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

//`````````````````````````````````````````````````Client`````````````````````````````````````````````````//
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

//`````````````````````````````````````````````````Subscription`````````````````````````````````````````````````//
    @Override
    public Future<String> addSubscription(String subscription) {
        return genericFunction(SocketController.ADD_SUBSCRIPTION, subscription);
    }

    @Override
    public Future<String> deleteSubscription(String id) {
        return genericFunction(SocketController.DELETE_SUBSCRIPTION, id);
    }

    @Override
    public Future<String> updateSubscription(String subscription) {
        return genericFunction(SocketController.UPDATE_SUBSCRIPTION, subscription);
    }

    @Override
    public Future<String> getAllSubscriptions() {
        return genericFunction(SocketController.GET_ALL_SUBSCRIPTIONS, null);
    }

    @Override
    public Future<String> filterSubscriptionByDuration(String duration) {
        return genericFunction(SocketController.FILTER_SUBSCRIPTION_BY_DURATION, duration);
    }

    @Override
    public Future<String> filterSubscriptionByType(String type) {
        return genericFunction(SocketController.FILTER_SUBSCRIPTION_BY_TYPE, type);
    }

//`````````````````````````````````````````````````Contract`````````````````````````````````````````````````//

    public Future<String> genericFunction(String command, String parameter){
        return executorService.submit( () -> {
            Message request = new Message(command, parameter);
            Message response = tcpClient.sendAndReceive(request);

            return response.getBody();
        });
    }
}
