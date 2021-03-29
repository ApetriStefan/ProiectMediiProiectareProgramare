package intrusii.client.Service;

import intrusii.common.Domain.Client;
import intrusii.common.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.Future;

public class ClientServiceClient implements ClientService {

    @Autowired
    private ClientService clientService;

    @Override
    public void addClient(Client client) {
        clientService.addClient(client);
    }

    @Override
    public void deleteClient(Long id) {
        clientService.deleteClient(id);
    }

    @Override
    public void updateClient(Client client) {
        clientService.updateClient(client);
    }

    @Override
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @Override
    public List<Client> filterClientsByName(String name) {
        return clientService.filterClientsByName(name);
    }

    @Override
    public List<Client> filterClientsByCnp(String cnp) {
        return clientService.filterClientsByCnp(cnp);
    }
}
