package intrusii.common;

import java.util.concurrent.Future;

public interface SocketSubscriptionService
{


    //`````````````````````````````````````````````````Subscription`````````````````````````````````````````````````//
    Future<String> addSubscription(String subscription);
    Future<String> deleteSubscription(String id);
    Future<String> updateSubscription(String subscription);
    Future<String> getAllSubscriptions();
    Future<String> filterSubscriptionByDuration(String duration);
    Future<String> filterSubscriptionByType(String type);

    String ADD_SUBSCRIPTION = "addSubscription";
    String DELETE_SUBSCRIPTION = "deleteSubscription";
    String UPDATE_SUBSCRIPTION = "updateSubscription";
    String GET_ALL_SUBSCRIPTIONS = "getAllSubscriptions";
    String FILTER_SUBSCRIPTION_BY_DURATION= "filterSubscriptionByDuration";
    String FILTER_SUBSCRIPTION_BY_TYPE= "filterSubscriptionByType";
}
