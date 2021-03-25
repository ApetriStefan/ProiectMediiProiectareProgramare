package intrusii.client.Service;

import intrusii.client.TCP.TcpClient;
import intrusii.common.Message;
import intrusii.common.SocketController;
import intrusii.common.SocketSubscriptionService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class SocketSubscriptionServiceImpl implements SocketSubscriptionService {
    private ExecutorService executorService;
    private TcpClient tcpClient;

    public SocketSubscriptionServiceImpl (ExecutorService executorService, TcpClient tcpClient){
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

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

    public Future<String> genericFunction(String command, String parameter){
        return executorService.submit( () -> {
            Message request = new Message(command, parameter);
            Message response = tcpClient.sendAndReceive(request);

            return response.getBody();
        });
    }

}
