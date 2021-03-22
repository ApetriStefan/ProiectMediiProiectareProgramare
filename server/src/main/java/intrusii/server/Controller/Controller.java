package intrusii.server.Controller;

import intrusii.server.Service.ClientService;
import intrusii.server.Service.ContractService;
import intrusii.server.Service.SubscriptionService;
import intrusii.server.Domain.Client;
import intrusii.server.Domain.Contract;
import intrusii.server.Domain.Subscription;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Controller {
    private ClientService clientService;
    private ContractService contractService;
    private SubscriptionService subscriptionService;

    public Controller(ClientService clientService, ContractService contractService, SubscriptionService subscriptionService) {
        this.clientService = clientService;
        this.contractService = contractService;
        this.subscriptionService = subscriptionService;
    }
//`````````````````````````````````````````````````Client`````````````````````````````````````````````````//
    public void addClient(Client client) {
        clientService.addClient(client);
    }

    /**
     * Deletes a client, and also all the contracts that the client has.
     *
     *
     * @param id
     *            must not be null.
     */
    public void deleteClient(Long id) {
        contractService.deleteContractsByClientID(id);
        clientService.deleteClient(id);
    }

    public void updateClient(Client client) {
        clientService.updateClient(client);
    }

    public Set<Client> getAllClients()
    {
        return clientService.getAllClients();
    }

    public List<Client> filterClientsByName(String name) {
        return clientService.filteredByClientName(name);
    }

    public List<Client> filterClientsByCnp(String cnp) {
        return clientService.filteredByClientCNP(cnp);
    }

//`````````````````````````````````````````````````Subscription`````````````````````````````````````````````````//
    public void addSubscription(Subscription subscription)
{
    subscriptionService.addSubscription(subscription);
}

    /**
     * Deletes a subscription, and also all the contracts that the subscription has.
     *
     *
     * @param id
     *            must not be null.
     */
    public void deleteSubscription(Long id) {
        contractService.deleteContractsBySubscriptionID(id);
        subscriptionService.deleteSubscription(id);
    }

    public void updateSubscription(Subscription subscription) {
        subscriptionService.updateSubscription(subscription);
    }


    public Set<Subscription> getAllSubscriptions()
    {
        return subscriptionService.getAllSubscriptions();
    }

    public List<Subscription> filterSubscriptionByDuration(int duration) {
        return subscriptionService.filterByDuration(duration);
}

    public List<Subscription> filterSubscriptionByType(String type){
        return subscriptionService.filterByType(type);
    }

//`````````````````````````````````````````````````Contract`````````````````````````````````````````````````//
    /**
     * Adds the contract if there is a valid client and subscription with the given IDs
     *
     *
     * @param contract
     *            must not be null.
     * @return an {@code boolean} - True if the entity was added, otherwise false.
     */
    public boolean addContract(Contract contract) {
        if(clientService.getClientByID(contract.getClientId()) != null)
            if(subscriptionService.getSubscriptionByID(contract.getSubscriptionId()) != null) {
                contractService.addContract(contract);
                return contractService.getContractById(contract.getId()) != null;
            }
        return false;
    }

    public void deleteContract(Long id) {
        contractService.deleteContract(id);
    }

    public void updateContract(Contract contract){
        clientService.getClientByID(contract.getClientId());
        contractService.updateContract(contract);
    }

    public Set<Contract> getAllContracts()
    {
        return contractService.getAllContracts();
    }

    public List<Contract> filterActiveContracts() {
        Set<Contract> contracts = contractService.getAllContracts();
        return contracts.stream().filter(c -> contractService.verifyActiveContract(c, subscriptionService.getSubscriptionByID(c.getSubscriptionId()).getDuration())).collect(Collectors.toList());
    }
}
