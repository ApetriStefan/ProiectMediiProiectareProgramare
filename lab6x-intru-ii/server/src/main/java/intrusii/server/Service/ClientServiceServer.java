package intrusii.server.Service;

import intrusii.common.Domain.Client;
import intrusii.common.Domain.Contract;
import intrusii.common.Service.ClientService;
import intrusii.common.Service.ServiceException;
import intrusii.common.Domain.Validators.ValidatorException;
import intrusii.server.Repository.Repository;
import org.springframework.expression.spel.CodeFlow;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class ClientServiceServer implements ClientService {
    private final Repository<Long, Client> repository;
    private final Repository<Long, Contract> contractRepository;

    public ClientServiceServer(Repository<Long, Client> repository, Repository<Long, Contract> contractRepository ) {
        this.repository = repository;
        this.contractRepository = contractRepository;
    }

    public void addClient(Client client) throws ValidatorException {
        repository.save(client);
    }

    public void deleteClient(Long id) throws ValidatorException, ServiceException {
        deleteContractByClientID(id);
        repository.delete(id).orElseThrow(() -> new ServiceException("There is no client with this ID"));
    }

    public void updateClient(Client client) throws ValidatorException, ServiceException {
        repository.update(client).orElseThrow(() -> new ServiceException("There is no client with this ID"));
    }

    public Client getClientByID(Long id) {
        return repository.findOne(id).orElseThrow(() -> new ServiceException("There is no client with this id"));
    }
    public List<Client> getAllClients() {
        Iterable<Client> clients = repository.findAll();
        return StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toList());
    }

    private void deleteContractByClientID(Long id) {
        StreamSupport.stream(contractRepository.findAll().spliterator(), false).filter(contract -> contract.getClientId().equals(id)).forEach(contract -> contractRepository.delete(contract.getId()));
    }

    public List<Client> filterClientsByName(String name) {
        return filterGeneric(x->x.getName().toLowerCase().contains(name.toLowerCase()));
    }

    public List<Client> filterClientsByCnp(String cnp) {
       return filterGeneric(x -> x.getCnp().equals(cnp));
    }

    private List<Client> filterGeneric(Predicate<Client> function){
        Iterable<Client> clientsIterable;
        clientsIterable = repository.findAll();
        return StreamSupport.stream(clientsIterable.spliterator(), false).filter(function).collect(Collectors.toList());
    }
}
