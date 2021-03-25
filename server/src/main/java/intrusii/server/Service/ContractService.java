package intrusii.server.Service;


import intrusii.server.Domain.Client;
import intrusii.server.Domain.Contract;
import intrusii.server.Domain.Subscription;
import intrusii.server.Domain.Validators.ValidatorException;
import intrusii.server.Repository.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ContractService {
    private Repository<Long, Contract> repository;
    private Repository<Long, Client> clientRepository;
    private Repository<Long, Subscription> subscriptionRepository;

    public ContractService(Repository<Long, Contract> repository,Repository<Long, Client> clientRepository,Repository<Long, Subscription> subscriptionRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public Set<Contract> getAllContracts() {
        Iterable<Contract> contracts = repository.findAll();
        return StreamSupport.stream(contracts.spliterator(), false).collect(Collectors.toSet());
    }

    public void addContract(Contract contract) throws ValidatorException
    {
        if (clientRepository.findOne(contract.getClientId()).isPresent() && subscriptionRepository.findOne(contract.getSubscriptionId()).isPresent())
            repository.save(contract);
    }

    public void deleteContract(Long id) throws ValidatorException {
        repository.delete(id);
    }

    /**
     * Updates a contract in the repository. Only the client can be updated, the rest of the attributes remain the same
     *
     * @param contract
     *            must not be null.
     */
    public void updateContract(Contract contract) throws ValidatorException {
        if (clientRepository.findOne(contract.getClientId()).isPresent()) {
            Optional<Contract> oldContract = repository.findOne(contract.getId());
            oldContract.orElseThrow(() -> new IllegalArgumentException("There is no contract with this ID"));
            oldContract.get().setClientId(contract.getClientId());
            repository.update(oldContract.get());
        }
        else
            throw new ValidatorException("This client does not exist!");

    }

    /**
     * Deletes entries in the repository by given Client ID (Long ID)
     *
     * @param id
     *            must not be null.
     */
    public void deleteContractsByClientID(Long id) {
        filteredByClientID(id).forEach(contract -> deleteContract(contract.getId()));
    }

    /**
     * Deletes entries in the repository by given Subscription ID (Long ID)
     *
     * @param id
     *            must not be null.
     */
    public void deleteContractsBySubscriptionID(Long id) {
        filteredBySubscriptionID(id).forEach(contract -> deleteContract(contract.getId()));
    }

    public Contract getContractById(Long id) {
        return repository.findOne(id).orElseThrow(() -> new IllegalArgumentException("There is no contract with this ID"));
    }

    /**
     * Filters the repository by given Client ID (Long ID)
     *
     * @param id
     *            must not be null.
     * @return an {@code List<Contract>} - if any contracts with specific Client ID were found, otherwise returns null.
     */
    public List<Contract> filteredByClientID(Long id) {
        return filterGeneric(x-> x.getClientId().equals(id));
    }

    /**
     * Filters the repository by given Subscription ID (Long ID)
     *
     * @param id
     *            must not be null.
     * @return an {@code List<Contract>} - if any contracts with specific Subscription ID were found, otherwise returns null.
     */
    public List<Contract> filteredBySubscriptionID(Long id) {
        return filterGeneric(x-> x.getSubscriptionId().equals(id));
    }

    public List<Contract> filterGeneric(Predicate<Contract> function){
        Iterable<Contract> contractsIterable;
        contractsIterable = repository.findAll();
        return StreamSupport.stream(contractsIterable.spliterator(), false).filter(function).collect(Collectors.toList());
    }

    /**
     * Filters the repository by given Subscription ID (Long ID)
     *
     * @return an {@code List<Contract>} - if any contracts with specific Subscription ID were found, otherwise returns null.
     */
    public boolean verifyActiveContract(Contract contract, int duration) {
        return ChronoUnit.MONTHS.between(contract.getDate(), LocalDate.now()) <= duration;
    }
}
