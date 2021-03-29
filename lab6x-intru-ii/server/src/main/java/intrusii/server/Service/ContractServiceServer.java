package intrusii.server.Service;

import intrusii.common.Domain.Client;
import intrusii.common.Domain.Contract;
import intrusii.common.Domain.Subscription;
import intrusii.common.Service.ContractService;
import intrusii.common.Service.ServiceException;
import intrusii.common.Domain.Validators.ValidatorException;
import intrusii.server.Repository.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ContractServiceServer implements ContractService{
    private final Repository<Long, Contract> repository;
    private final Repository<Long, Client> clientRepository;
    private final Repository<Long, Subscription> subscriptionRepository;

    public ContractServiceServer(Repository<Long, Contract> repository, Repository<Long, Client> clientRepository, Repository<Long, Subscription> subscriptionRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public void addContract(Contract contract) throws ValidatorException, ServiceException {
        if (clientRepository.findOne(contract.getClientId()).isPresent() && subscriptionRepository.findOne(contract.getSubscriptionId()).isPresent())
            repository.save(contract);
        else
            throw new ServiceException("The ids are not valid");
    }

    public void deleteContract(Long id) throws ValidatorException {
        repository.delete(id);
    }

    public void updateContract(Contract contract) throws ValidatorException, ServiceException {
        if (clientRepository.findOne(contract.getClientId()).isPresent()) {
            Optional<Contract> oldContract = repository.findOne(contract.getId());
            oldContract.orElseThrow(() -> new ServiceException("There is no contract with this ID"));
            oldContract.get().setClientId(contract.getClientId());
            repository.update(oldContract.get());
        }
        else
            throw new ServiceException("This client does not exist");
    }

    public List<Contract> getAllContracts() {
        Iterable<Contract> contracts = repository.findAll();
        return StreamSupport.stream(contracts.spliterator(), false).collect(Collectors.toList());
    }

    public List<Contract> filterActiveContracts() {
        return filterGeneric(x -> ChronoUnit.MONTHS.between(x.getDate(), LocalDate.now()) <= subscriptionRepository.findOne(x.getSubscriptionId()).get().getDuration());
    }

    public List<Contract> filterGeneric(Predicate<Contract> function){
        Iterable<Contract> contractsIterable;
        contractsIterable = repository.findAll();
        return StreamSupport.stream(contractsIterable.spliterator(), false).filter(function).collect(Collectors.toList());
    }
}
