package intrusii.server.Service;

import intrusii.server.Domain.Contract;
import intrusii.server.Domain.Subscription;
import intrusii.server.Domain.Validators.ValidatorException;
import intrusii.server.Repository.Repository;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class SubscriptionService {
    private final Repository<Long, Subscription> repository;
    private final Repository<Long, Contract> contractRepository;

    public SubscriptionService(Repository<Long, Subscription> repository,Repository<Long, Contract> contractRepository) {
        this.repository = repository;
        this.contractRepository = contractRepository;
    }

    public Set<Subscription> getAllSubscriptions() {
        Iterable<Subscription> subscriptions = repository.findAll();
        return StreamSupport.stream(subscriptions.spliterator(), false).collect(Collectors.toSet());
    }

    public void addSubscription(Subscription subscription) throws ValidatorException {
        repository.save(subscription);
    }

    public void deleteSubscription(Long id) throws ValidatorException {
        deleteContractBySubscriptionID(id);
        repository.delete(id).orElseThrow(() -> new IllegalArgumentException("There is no subscription with this ID"));
    }

    public void updateSubscription(Subscription subscription) {
        repository.update(subscription).orElseThrow(() -> new IllegalArgumentException("There is no subscription with this ID"));
    }

    public Subscription getSubscriptionByID(Long id) {
        return repository.findOne(id).orElseThrow(() -> new IllegalArgumentException("ClientID not found"));
    }

    public List<Subscription> filterByDuration(int duration){
        return filterGeneric(x -> x.getDuration() == duration);
    }

    public List<Subscription> filterByType(String type){
        return filterGeneric(x -> x.getType().getLabel().equals(type));
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
