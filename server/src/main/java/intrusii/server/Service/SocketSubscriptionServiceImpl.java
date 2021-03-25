package intrusii.server.Service;
import intrusii.common.*;
import intrusii.server.Domain.Subscription;
import intrusii.server.Domain.Validators.ValidatorException;
import intrusii.server.Utility.SubscriptionUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


public class SocketSubscriptionServiceImpl implements SocketSubscriptionService {
    private final ExecutorService executorService;
    private final SubscriptionService subscriptionService;

    public SocketSubscriptionServiceImpl(ExecutorService executorService, SubscriptionService subscriptionService) {
        this.executorService = executorService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public Future<String> addSubscription(String subscription) {

        return executorService.submit( () -> {
            try{
                Subscription subscriptionObj = SubscriptionUtil.StringToSubscription(subscription);
                subscriptionService.addSubscription(subscriptionObj);
                return "Subscription successfully added";
            }catch (SocketException | ValidatorException e) {
                return e.getMessage();
            }
        });
    }

    @Override
    public Future<String> deleteSubscription(String id) {

        return executorService.submit( () -> {
            try{
                Long idLong = Long.parseLong(id);
                subscriptionService.deleteSubscription(idLong);
                return "Subscription successfully deleted";
            }catch (SocketException |ValidatorException e){
                return e.getMessage();
            }
            catch (NumberFormatException e){
                return "The id should be an integer";
            }
        });
    }

    @Override
    public Future<String> updateSubscription(String subscription) {

        return executorService.submit( () -> {
            try{
                Subscription subscriptionObj = SubscriptionUtil.StringToSubscriptionWithId(subscription);
                subscriptionService.updateSubscription(subscriptionObj);
                return "Subscription successfully updated";
            }catch (SocketException | ValidatorException e){
                return e.getMessage();
            }
        });
    }

    @Override
    public Future<String> getAllSubscriptions() {

        return executorService.submit( () -> {
            Set<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
            return "The subscriptions are:;" + SubscriptionUtil.SetToString(subscriptions);
        });
    }

    @Override
    public Future<String> filterSubscriptionByDuration(String duration) {

        return executorService.submit( () -> {
            List<Subscription> subscriptionList = subscriptionService.filterByDuration(Integer.parseInt(duration));
            Set<Subscription> subscriptionSet = new HashSet<>(subscriptionList);
            return "The subscription with duration '" + duration + "' are:;" + SubscriptionUtil.SetToString(subscriptionSet);
        });
    }

    @Override
    public Future<String> filterSubscriptionByType(String type) {

        return executorService.submit( () -> {
            List<Subscription> subscriptionList = subscriptionService.filterByType(type);
            Set<Subscription> subscriptionSet = new HashSet<>(subscriptionList);
            return "The subscription of type '" + type + "' are:;" + SubscriptionUtil.SetToString(subscriptionSet);
        });
    }
}
