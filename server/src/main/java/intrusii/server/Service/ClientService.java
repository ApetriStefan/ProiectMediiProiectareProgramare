package intrusii.server.Service;

import intrusii.server.Domain.Client;
import intrusii.server.Domain.Validators.ValidatorException;
import intrusii.server.Repository.Repository;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class ClientService {
    private Repository<Long, Client> repository;

    public ClientService(Repository<Long, Client> repository) {
        this.repository = repository;
    }

    public Set<Client> getAllClients() {
        Iterable<Client> clients = repository.findAll();
        return StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toSet());
    }

    public void addClient(Client client) throws ValidatorException {
        repository.save(client);
    }

    public void deleteClient(Long id) throws ValidatorException {
        repository.delete(id).orElseThrow(() -> new IllegalArgumentException("There is no client with this ID"));
    }

    public void updateClient(Client client) throws ValidatorException {
        repository.update(client).orElseThrow(() -> new IllegalArgumentException("There is no client with this ID"));
    }

    public Client getClientByID(Long id) {
        return repository.findOne(id).orElseThrow(() -> new IllegalArgumentException("There is no client with this id"));
    }

    /**
     * Filters the repository by given Client Name (String Name)
     *
     * @param name
     *            must not be null.
     * @return an {@code List<Client>} - if any clients with specific Client name were found, otherwise returns null.
     */
    public List<Client> filteredByClientName(String name) {
        return filterGeneric(x->x.getName().contains(name));
    }

    /**
     * Filters the repository by given Client CNP (String cnp)
     *
     * @param cnp
     *            must not be null.
     * @return an {@code List<Client>} - if any clients with specific Client cnp were found, otherwise returns null.
     */
    public List<Client> filteredByClientCNP(String cnp) {
       return filterGeneric(x -> x.getCnp().equals(cnp));
    }

    public List<Client> filterGeneric(Predicate<Client> function){
        Iterable<Client> clientsIterable;
        clientsIterable = repository.findAll();
        return StreamSupport.stream(clientsIterable.spliterator(), false).filter(function).collect(Collectors.toList());
    }
}
