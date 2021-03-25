package intrusii.server.Service;
import intrusii.common.*;
import intrusii.server.Domain.Client;
import intrusii.server.Domain.Validators.ValidatorException;
import intrusii.server.Utility.ClientUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class SocketClientServiceImpl implements SocketClientService{

    private final ExecutorService executorService;
    private final ClientService clientService;
    private final ContractService contractService;

    public SocketClientServiceImpl(ExecutorService executorService,ClientService clientService,ContractService contractService) {
        this.executorService = executorService;
        this.clientService = clientService;
        this.contractService = contractService;
    }


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

}
