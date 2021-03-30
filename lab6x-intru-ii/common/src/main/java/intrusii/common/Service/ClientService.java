package intrusii.common.Service;

import intrusii.common.Domain.Client;

import java.util.List;

public interface ClientService {

    void addClient(Client client);

    void deleteClient(Long id);

    void updateClient(Client client);

    Client getClientByID(Long id);

    List<Client> getAllClients();

    List<Client> filterClientsByName(String name);

    List<Client> filterClientsByCnp(String cnp);
}
