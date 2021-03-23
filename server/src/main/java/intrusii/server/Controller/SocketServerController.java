package intrusii.server.Controller;

import intrusii.common.SocketController;
import intrusii.common.SocketException;
import intrusii.server.Domain.Client;
import intrusii.server.Domain.Contract;
import intrusii.server.Domain.Subscription;
import intrusii.server.Domain.Validators.ValidatorException;
import intrusii.server.Service.ClientService;
import intrusii.server.Service.ContractService;
import intrusii.server.Service.SubscriptionService;
import intrusii.server.Utility.ClientUtil;
import intrusii.server.Utility.ContractUtil;
import intrusii.server.Utility.SubscriptionUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

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
            }catch (SocketException |ValidatorException e){
                return e.getMessage();
            }
            catch (NumberFormatException e){
                return "The id should be an integer";
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
    @Override
    public Future<String> addSubscription(String subscription) {

        return executorService.submit( () -> {
            try{
                Subscription subscriptionObj = SubscriptionUtil.StringToSubscription(subscription);
                subscriptionService.addSubscription(subscriptionObj);
                return "Subscription successfully added";
            }catch (SocketException | ValidatorException e) {
                return e.getMessage();
            }
        });
    }

    @Override
    public Future<String> deleteSubscription(String id) {

        return executorService.submit( () -> {
            try{
                Long idLong = Long.parseLong(id);
                contractService.deleteContractsBySubscriptionID(idLong);
                subscriptionService.deleteSubscription(idLong);
                return "Subscription successfully deleted";
            }catch (SocketException |ValidatorException e){
                return e.getMessage();
            }
            catch (NumberFormatException e){
                return "The id should be an integer";
            }
        });
    }

    @Override
    public Future<String> updateSubscription(String subscription) {

        return executorService.submit( () -> {
            try{
                Subscription subscriptionObj = SubscriptionUtil.StringToSubscriptionWithId(subscription);
                subscriptionService.updateSubscription(subscriptionObj);
                return "Subscription successfully updated";
            }catch (SocketException | ValidatorException e){
                return e.getMessage();
            }
        });
    }

    @Override
    public Future<String> getAllSubscriptions() {

        return executorService.submit( () -> {
            Set<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
            return SubscriptionUtil.SetToString(subscriptions);
        });
    }

    @Override
    public Future<String> filterSubscriptionByDuration(String duration) {

        return executorService.submit( () -> {
            List<Subscription> subscriptionList = subscriptionService.filterByDuration(Integer.parseInt(duration));
            Set<Subscription> subscriptionSet = new HashSet<>(subscriptionList);
            return SubscriptionUtil.SetToString(subscriptionSet);
        });
    }

    @Override
    public Future<String> filterSubscriptionByType(String type) {

        return executorService.submit( () -> {
            List<Subscription> subscriptionList = subscriptionService.filterByType(type);
            Set<Subscription> subscriptionSet = new HashSet<>(subscriptionList);
            return SubscriptionUtil.SetToString(subscriptionSet);
        });
    }

//`````````````````````````````````````````````````Contract`````````````````````````````````````````````````//
    @Override
    public Future<String> addContract(String contract) {

        return executorService.submit( () -> {
            try{
                Contract contractObj = ContractUtil.StringToContract(contract);
                contractService.addContract(contractObj);
                return "Contract successfully added";
            }catch (SocketException | ValidatorException e) {
                return e.getMessage();
            }
        });
    }

    @Override
    public Future<String> deleteContract(String id) {

        return executorService.submit( () -> {
            try{
                Long idLong = Long.parseLong(id);
                contractService.deleteContract(idLong);
                return "Contract successfully deleted";
            }catch (SocketException |ValidatorException e){
                return e.getMessage();
            }
            catch (NumberFormatException e){
                return "The id should be an integer";
            }
        });
    }

    @Override
    public Future<String> updateContract(String contract) {

        return executorService.submit( () -> {
            try{
                Contract contractObj = ContractUtil.StringToContractWithId(contract);
                contractService.updateContract(contractObj);
                return "Contract successfully updated";
            }catch (SocketException | ValidatorException e){
                return e.getMessage();
            }
        });
    }

    @Override
    public Future<String> getAllContracts() {

        return executorService.submit( () -> {
            Set<Contract> contracts = contractService.getAllContracts();
            return ContractUtil.SetToString(contracts, clientService, subscriptionService);
        });
    }

    @Override
    public Future<String> filterExpiredContracts() {

        return executorService.submit( () -> {
            Set<Contract> contracts = contractService.getAllContracts();
            List<Contract> contractList = contracts.stream().filter(c -> contractService.verifyActiveContract(c, subscriptionService.getSubscriptionByID(c.getSubscriptionId()).getDuration())).collect(Collectors.toList());
            Set<Contract> contractSet = new HashSet<>(contractList);
            return ContractUtil.SetToString(contractSet, clientService, subscriptionService);
        });
    }
}


