package intrusii.server.Domain;

import java.util.HashMap;

public enum SubscriptionType {
    Internet("Internet"),
    TV("TV"),
    Phone("Phone"),
    Default("Default");

    public final String label;

    private SubscriptionType(String label)
    {
        this.label = label;
    }

    public String getLabel()
    {
        return this.label;
    }

    public SubscriptionType setSubscriptionType(String type){
        HashMap<String, SubscriptionType> dict = new HashMap<>();
        dict.put("Internet", SubscriptionType.Internet);
        dict.put("Phone", SubscriptionType.Phone);
        dict.put("TV", SubscriptionType.TV);
        return dict.get(type);
    }
}