package intrusii.common;

import java.util.concurrent.Future;

public interface SocketController {
    int PORT = 6666;
    String HOST = "localhost";

    String ADD_CLIENT = "addClient";
    String DELETE_CLIENT = "deleteClient";
    String UPDATE_CLIENT = "updateClient";
    String GET_ALL_CLIENTS = "getAllClients";
    String FILTER_CLIENTS_BY_NAME = "filterClientsByName";
    String FILTER_CLIENTS_BY_CNP = "filterClientsByCnp";

//`````````````````````````````````````````````````Client`````````````````````````````````````````````````//
    Future<String> addClient(String client);
    Future<String> deleteClient(String id);
    Future<String> updateClient(String client);
    Future<String> getAllClients();
    Future<String> filterClientsByName(String name);
    Future<String> filterClientsByCnp(String cnp);

//`````````````````````````````````````````````````Subscription`````````````````````````````````````````````````//


//`````````````````````````````````````````````````Contract`````````````````````````````````````````````````//


}
