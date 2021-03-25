package intrusii.common;

import java.util.concurrent.Future;

public interface SocketController {

    String ADD_CLIENT = "addClient";
    String DELETE_CLIENT = "deleteClient";
    String UPDATE_CLIENT = "updateClient";
    String GET_ALL_CLIENTS = "getAllClients";
    String FILTER_CLIENTS_BY_NAME = "filterClientsByName";
    String FILTER_CLIENTS_BY_CNP = "filterClientsByCnp";

    String ADD_SUBSCRIPTION = "addSubscription";
    String DELETE_SUBSCRIPTION = "deleteSubscription";
    String UPDATE_SUBSCRIPTION = "updateSubscription";
    String GET_ALL_SUBSCRIPTIONS = "getAllSubscriptions";
    String FILTER_SUBSCRIPTION_BY_DURATION= "filterSubscriptionByDuration";
    String FILTER_SUBSCRIPTION_BY_TYPE= "filterSubscriptionByType";

    String ADD_CONTRACT = "addContract";
    String DELETE_CONTRACT = "deleteContract";
    String UPDATE_CONTRACT = "updateContract";
    String GET_ALL_CONTRACTS = "getAllContracts";
    String FILTER_EXPIRED_CONTRACTS= "filterExpiredContracts";



//`````````````````````````````````````````````````Client`````````````````````````````````````````````````//
    Future<String> addClient(String client);
    Future<String> deleteClient(String id);
    Future<String> updateClient(String client);
    Future<String> getAllClients();
    Future<String> filterClientsByName(String name);
    Future<String> filterClientsByCnp(String cnp);

//`````````````````````````````````````````````````Subscription`````````````````````````````````````````````````//
    Future<String> addSubscription(String subscription);
    Future<String> deleteSubscription(String id);
    Future<String> updateSubscription(String subscription);
    Future<String> getAllSubscriptions();
    Future<String> filterSubscriptionByDuration(String duration);
    Future<String> filterSubscriptionByType(String type);


//`````````````````````````````````````````````````Contract`````````````````````````````````````````````````//

    Future<String> addContract(String contract);
    Future<String> deleteContract(String id);
    Future<String> updateContract(String contract);
    Future<String> getAllContracts();
    Future<String> filterExpiredContracts();

}
