package intrusii.common;

import java.util.concurrent.Future;

public interface SocketClientService {

    String ADD_CLIENT = "addClient";
    String DELETE_CLIENT = "deleteClient";
    String UPDATE_CLIENT = "updateClient";
    String GET_ALL_CLIENTS = "getAllClients";
    String FILTER_CLIENTS_BY_NAME = "filterClientsByName";
    String FILTER_CLIENTS_BY_CNP = "filterClientsByCnp";

    Future<String> addClient(String client);
    Future<String> deleteClient(String id);
    Future<String> updateClient(String client);
    Future<String> getAllClients();
    Future<String> filterClientsByName(String name);
    Future<String> filterClientsByCnp(String cnp);
}
