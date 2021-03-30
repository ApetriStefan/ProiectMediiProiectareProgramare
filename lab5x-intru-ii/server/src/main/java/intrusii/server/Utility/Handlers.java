package intrusii.server.Utility;

import intrusii.common.*;
import intrusii.server.TCP.TcpServer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Handlers {
    public static void addHandlerClient(TcpServer tcpServer, SocketClientService socketClientService) {
        //addClient
        tcpServer.addHandler(SocketClientService.ADD_CLIENT, request -> {
            Future<String> res = socketClientService.addClient(request.getBody());
            return addHandlerTry(res);
        });
        //deleteClient
        tcpServer.addHandler(SocketClientService.DELETE_CLIENT, request -> {
            Future<String> res = socketClientService.deleteClient(request.getBody());
            return addHandlerTry(res);
        });
        //updateClient
        tcpServer.addHandler(SocketClientService.UPDATE_CLIENT, request -> {
            Future<String> res = socketClientService.updateClient(request.getBody());
            return addHandlerTry(res);
        });
        //getAllClients
        tcpServer.addHandler(SocketClientService.GET_ALL_CLIENTS, request -> {
            Future<String> res = socketClientService.getAllClients();
            return addHandlerTry(res);
        });
        //filterClientsByName
        tcpServer.addHandler(SocketClientService.FILTER_CLIENTS_BY_NAME, request -> {
            Future<String> res = socketClientService.filterClientsByName(request.getBody());
            return addHandlerTry(res);
        });
        //filterClientsByCnp
        tcpServer.addHandler(SocketClientService.FILTER_CLIENTS_BY_CNP, request -> {
            Future<String> res = socketClientService.filterClientsByCnp(request.getBody());
            return addHandlerTry(res);
        });
    }

    public static void addHandlerSubscription(TcpServer tcpServer, SocketSubscriptionService socketSubscriptionService) {
        //addSubscription
        tcpServer.addHandler(SocketSubscriptionService.ADD_SUBSCRIPTION, request -> {
            Future<String> res = socketSubscriptionService.addSubscription(request.getBody());
            return addHandlerTry(res);
        });
        //deleteSubscription
        tcpServer.addHandler(SocketSubscriptionService.DELETE_SUBSCRIPTION, request -> {
            Future<String> res = socketSubscriptionService.deleteSubscription(request.getBody());
            return addHandlerTry(res);
        });
        //updateSubscription
        tcpServer.addHandler(SocketSubscriptionService.UPDATE_SUBSCRIPTION, request -> {
            Future<String> res = socketSubscriptionService.updateSubscription(request.getBody());
            return addHandlerTry(res);
        });
        //getAllSubscriptions
        tcpServer.addHandler(SocketSubscriptionService.GET_ALL_SUBSCRIPTIONS, request -> {
            Future<String> res = socketSubscriptionService.getAllSubscriptions();
            return addHandlerTry(res);
        });
        //filterSubscriptionsByDuration
        tcpServer.addHandler(SocketSubscriptionService.FILTER_SUBSCRIPTION_BY_DURATION, request -> {
            Future<String> res = socketSubscriptionService.filterSubscriptionByDuration(request.getBody());
            return addHandlerTry(res);
        });
        //filterSubscriptionsByType
        tcpServer.addHandler(SocketSubscriptionService.FILTER_SUBSCRIPTION_BY_TYPE, request -> {
            Future<String> res = socketSubscriptionService.filterSubscriptionByType(request.getBody());
            return addHandlerTry(res);
        });

    }

    public static void addHandlerContract(TcpServer tcpServer, SocketContractService socketContractService) {
        //addContract
        tcpServer.addHandler(SocketContractService.ADD_CONTRACT, request -> {
            Future<String> res = socketContractService.addContract(request.getBody());
            return addHandlerTry(res);
        });
        //deleteContract
        tcpServer.addHandler(SocketContractService.DELETE_CONTRACT, request -> {
            Future<String> res = socketContractService.deleteContract(request.getBody());
            return addHandlerTry(res);
        });
        //updateContract
        tcpServer.addHandler(SocketContractService.UPDATE_CONTRACT, request -> {
            Future<String> res = socketContractService.updateContract(request.getBody());
            return addHandlerTry(res);
        });
        //getAllContracts
        tcpServer.addHandler(SocketContractService.GET_ALL_CONTRACTS, request -> {
            Future<String> res = socketContractService.getAllContracts();
            return addHandlerTry(res);
        });
        //filterExpiredContracts
        tcpServer.addHandler(SocketContractService.FILTER_EXPIRED_CONTRACTS, request -> {
            Future<String> res = socketContractService.filterActiveContracts();
            return addHandlerTry(res);
        });
    }

    private static Message addHandlerTry(Future<String> res) {
        try {
            CompletableFuture<Message> completableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    String result = res.get();
                    return new Message(Message.OK, result);
                } catch (InterruptedException | ExecutionException e) {
                    return new Message(Message.ERROR, e.getMessage());
                }
            });
            return completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            return new Message(Message.ERROR, e.getMessage());

//        try {
//            CompletableFuture.supplyAsync(() -> socketContractService.updateContract(contract)).thenAccept(this::printResponse);
//
//            String result = res.get();
//            Message response = new Message(Message.OK, result);
//            return response;
//        } catch (InterruptedException | ExecutionException e) {
//            return new Message(Message.ERROR, e.getMessage());
//        }
        }
    }
}
