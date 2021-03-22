package intrusii.server.Controller;

import intrusii.common.SocketController;
import intrusii.common.SocketException;
import intrusii.server.Domain.Client;
import intrusii.server.Domain.Validators.ValidatorException;
import intrusii.server.Service.ClientService;
import intrusii.server.Service.ContractService;
import intrusii.server.Service.SubscriptionService;
import intrusii.server.Utility.ClientUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class SocketServerController implements SocketController {
    private final ExecutorService executorService;
    private ClientService clientService;
    private ContractService contractService;
    private SubscriptionService subscriptionService;

    public SocketServerController(ExecutorService executorService, ClientService clientService, ContractService contractService, SubscriptionService subscriptionService){
        this.executorService = executorService;
        this.clientService = clientService;
        this.contractService = contractService;
        this.subscriptionService = subscriptionService;
    }

//`````````````````````````````````````````````````Client`````````````````````````````````````````````````//
    @Override
    public Future<String> addClient(String client) {

        return executorService.submit( () -> {
            try{
                Client clientObj = ClientUtil.StringToClient(client);
                clientService.addClient(clientObj);
                return "Client successfully added";
            }catch (SocketException | ValidatorException e) {
                return e.getMessage();
            }
        });
    }

    @Override
    public Future<String> deleteClient(String id) {

        return executorService.submit( () -> {
            try{
                Long idLong = Long.parseLong(id);
                contractService.deleteContractsByClientID(idLong);
                clientService.deleteClient(idLong);
                return "Client successfully deleted";
            }catch (SocketException |ValidatorException | NumberFormatException e){
                return e.getMessage();
            }
        });
    }

    @Override
    public Future<String> updateClient(String client) {

        return executorService.submit( () -> {
            try{
                Client clientObj = ClientUtil.StringToClientWithId(client);
                clientService.updateClient(clientObj);
                return "Client successfully updated";
            }catch (SocketException | ValidatorException e){
                return e.getMessage();
            }
        });
    }

    @Override
    public Future<String> getAllClients() {

        return executorService.submit( () -> {
            Set<Client> clients = clientService.getAllClients();
            return ClientUtil.SetToString(clients);
        });
    }

    @Override
    public Future<String> filterClientsByName(String name) {

        return executorService.submit( () -> {
           List<Client> clientList = clientService.filteredByClientName(name);
           Set<Client> clientSet = new HashSet<>(clientList);
            return ClientUtil.SetToString(clientSet);
        });
    }

    @Override
    public Future<String> filterClientsByCnp(String cnp) {
        return executorService.submit( () -> {
            List<Client> clientList = clientService.filteredByClientCNP(cnp);
            Set<Client> clientSet = new HashSet<>(clientList);
            return ClientUtil.SetToString(clientSet);
        });
    }

//`````````````````````````````````````````````````Subscription`````````````````````````````````````````````````//

//`````````````````````````````````````````````````Contract`````````````````````````````````````````````````//
}


