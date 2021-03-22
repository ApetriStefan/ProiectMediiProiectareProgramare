package intrusii.server.Utility;

import intrusii.common.Message;
import intrusii.common.SocketController;
import intrusii.server.TCP.TcpServer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Handlers {
    public static void addHandlerClient(TcpServer tcpServer, SocketController socketController) {
        //addClient
        tcpServer.addHandler(SocketController.ADD_CLIENT, request -> {
            Future<String> res = socketController.addClient(request.getBody());
            return addHandlerTry(res);
        });
        //deleteClient
        tcpServer.addHandler(SocketController.DELETE_CLIENT, request -> {
            Future<String> res = socketController.deleteClient(request.getBody());
            return addHandlerTry(res);
        });
        //updateClient
        tcpServer.addHandler(SocketController.UPDATE_CLIENT, request -> {
            Future<String> res = socketController.updateClient(request.getBody());
            return addHandlerTry(res);
        });
        //getAllClients
        tcpServer.addHandler(SocketController.GET_ALL_CLIENTS, request -> {
            Future<String> res = socketController.getAllClients();
            return addHandlerTry(res);
        });
        //filterClientsByName
        tcpServer.addHandler(SocketController.FILTER_CLIENTS_BY_NAME, request -> {
            Future<String> res = socketController.filterClientsByName(request.getBody());
            return addHandlerTry(res);
        });
        //filterClientsByCnp
        tcpServer.addHandler(SocketController.FILTER_CLIENTS_BY_CNP, request -> {
            Future<String> res = socketController.filterClientsByCnp(request.getBody());
            return addHandlerTry(res);
        });
    }

    private static Message addHandlerTry(Future<String> res) {
        try {
            String result = res.get();
            Message response = new Message(Message.OK, result);
            return response;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Message(Message.ERROR, e.getMessage());
        }
    }

//    private static void addHandlerGeneric(TcpServer tcpServer, SocketController socketController, String functionName, Future<String> functionResult, Function<Void, Future<String>> function){
//        tcpServer.addHandler(functionName, request -> {
//            Future<String> res = socketController.function(request.getBody());
//            try{
//                String result = res.get();
//                Message response = new Message(Message.OK, result);
//                return response;
//            }catch (InterruptedException | ExecutionException e){
//                e.printStackTrace();
//                return new Message(Message.ERROR, e.getMessage());
//            }
//        });
//    }
}
