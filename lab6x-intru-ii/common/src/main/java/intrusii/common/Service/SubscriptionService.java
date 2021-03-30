package intrusii.common.Service;

import intrusii.common.Domain.Subscription;
import intrusii.common.Domain.SubscriptionType;

import java.util.List;

public interface SubscriptionService {

    void addSubscription(Subscription subscription);

    void deleteSubscription(Long id);

    void updateSubscription(Subscription subscription);

    Subscription getSubscriptionByID(Long id);

    List<Subscription> getAllSubscriptions();

    List<Subscription> filterSubscriptionByDuration(int duration);

    List<Subscription> filterSubscriptionByType(SubscriptionType type);
}
