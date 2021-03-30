package intrusii.server.Service;

import intrusii.common.Domain.Contract;
import intrusii.common.Domain.Subscription;
import intrusii.common.Domain.SubscriptionType;
import intrusii.common.Service.ServiceException;
import intrusii.common.Service.SubscriptionService;
import intrusii.common.Domain.Validators.ValidatorException;
import intrusii.server.Repository.Repository;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class SubscriptionServiceServer implements SubscriptionService {
    private final Repository<Long, Subscription> repository;
    private final Repository<Long, Contract> contractRepository;

    public SubscriptionServiceServer(Repository<Long, Subscription> repository, Repository<Long, Contract> contractRepository) {
        this.repository = repository;
        this.contractRepository = contractRepository;
    }

    public void addSubscription(Subscription subscription) throws ValidatorException {
        repository.save(subscription);
    }

    public void deleteSubscription(Long id) throws ValidatorException, ServiceException {
        deleteContractBySubscriptionID(id);
        repository.delete(id).orElseThrow(() -> new ServiceException("There is no subscription with this ID"));
    }

    public void updateSubscription(Subscription subscription) throws ValidatorException, ServiceException {
        repository.update(subscription).orElseThrow(() -> new ServiceException("There is no subscription with this ID"));
    }

    public Subscription getSubscriptionByID(Long id){
        return repository.findOne(id).orElseThrow(() -> new ServiceException("There is no subscription with this id"));
    }
    public List<Subscription> getAllSubscriptions() {
        Iterable<Subscription> subscriptions = repository.findAll();
        return StreamSupport.stream(subscriptions.spliterator(), false).collect(Collectors.toList());
    }

    public List<Subscription> filterSubscriptionByDuration(int duration){
        return filterGeneric(x -> x.getDuration() == duration);
    }

    public List<Subscription> filterSubscriptionByType(SubscriptionType type){
        return filterGeneric(x -> x.getType().equals(type));
    }

    public List<Subscription> filterGeneric(Predicate<Subscription> function){
        Iterable<Subscription> subscriptionIterable;
        subscriptionIterable = repository.findAll();
        return StreamSupport.stream(subscriptionIterable.spliterator(), false).filter(function).collect(Collectors.toList());
    }

    private void deleteContractBySubscriptionID(Long id) {
        StreamSupport.stream(contractRepository.findAll().spliterator(), false).filter(contract -> contract.getSubscriptionId().equals(id)).forEach(contract -> contractRepository.delete(contract.getId()));
    }
}
