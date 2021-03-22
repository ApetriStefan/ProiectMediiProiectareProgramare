package intrusii.server.Controller;

import intrusii.common.SocketController;
import intrusii.server.Domain.Client;
import intrusii.server.Service.ClientService;
import intrusii.server.Service.ContractService;
import intrusii.server.Service.SubscriptionService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class SocketServerController implements SocketController {
    private ExecutorService executorService;
    private ClientService clientService;
    private ContractService contractService;
    private SubscriptionService subscriptionService;

    public SocketServerController(ExecutorService executorService, ClientService clientService, ContractService contractService, SubscriptionService subscriptionService){
        this.executorService = executorService;
        this.clientService = clientService;
        this.contractService = contractService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public Future<String> addClient(String client) {
        String[] arguments = client.split(";");
        String cnp =  arguments[0];
        String name = arguments[1];
        String email = arguments[2];
        String address = arguments[3];

        Client clientObj = new Client(cnp, name, email, address);

        return executorService.submit( () -> {
                    clientService.addClient(clientObj);
                    return "Client successfully added";
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
