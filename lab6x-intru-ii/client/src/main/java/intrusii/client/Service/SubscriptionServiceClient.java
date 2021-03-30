package intrusii.client.Service;

import intrusii.common.Domain.Subscription;
import intrusii.common.Domain.SubscriptionType;
import intrusii.common.Service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.Future;

public class SubscriptionServiceClient implements SubscriptionService {

    @Autowired
    private SubscriptionService subscriptionService;

    @Override
    public void addSubscription(Subscription subscription) {
        subscriptionService.addSubscription(subscription);
    }

    @Override
    public void deleteSubscription(Long id) {
        subscriptionService.deleteSubscription(id);
    }

    @Override
    public void updateSubscription(Subscription subscription) {
        subscriptionService.updateSubscription(subscription);
    }

    @Override
    public Subscription getSubscriptionByID(Long id) {
        return subscriptionService.getSubscriptionByID(id);
    }

    @Override
    public List<Subscription> getAllSubscriptions() {
        return subscriptionService.getAllSubscriptions();
    }

    @Override
    public List<Subscription> filterSubscriptionByDuration(int duration) {
        return subscriptionService.filterSubscriptionByDuration(duration);
    }

    @Override
    public List<Subscription> filterSubscriptionByType(SubscriptionType type) {
        return subscriptionService.filterSubscriptionByType(type);
    }
}
