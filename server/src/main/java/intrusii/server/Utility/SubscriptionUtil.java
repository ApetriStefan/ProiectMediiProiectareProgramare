package intrusii.server.Utility;

import intrusii.common.SocketException;
import intrusii.server.Domain.Client;
import intrusii.server.Domain.Subscription;
import intrusii.server.Domain.SubscriptionType;

import java.util.Set;

public class SubscriptionUtil {
    public static Subscription StringToSubscription(String subscription) {
        try {
            String[] arguments = subscription.split(";");
            String type = arguments[0];
            String price = arguments[1];
            String duration = arguments[2];

            return new Subscription(SubscriptionType.valueOf(type), Integer.parseInt(price), Integer.parseInt(duration));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new SocketException("Something went wrong! Check the input");
        }
    }

    public static Subscription StringToSubscriptionWithId(String subscription) {
        try {
            String[] arguments = subscription.split(";");
            String id = arguments[0];
            String type = arguments[1];
            String price = arguments[2];
            String duration = arguments[3];

            Long idLong = Long.parseLong(id);

            Subscription subscriptionObj = new Subscription(SubscriptionType.valueOf(type), Integer.parseInt(price), Integer.parseInt(duration));
            subscriptionObj.setId(idLong);
            return subscriptionObj;
        } catch (ArrayIndexOutOfBoundsException |  NumberFormatException e) {
            throw new SocketException("Something went wrong! Check the input");
        }
    }

    public static String SetToString(Set<Subscription> subscriptions) {
        return subscriptions.stream().map(Subscription::toString).reduce("", (subscriptionsString, subscription) -> subscriptionsString + ";" + subscription);
    }
}
