package intrusii.common.Domain;

import java.util.Objects;

public class Subscription extends BaseEntity<Long> {
    private SubscriptionType type;
    private float price;
    private int duration; //months

    public Subscription() {
    }

    public Subscription(SubscriptionType type, float price, int duration) {
        this.type = type;
        this.price = price;
        this.duration = duration;
    }

    public SubscriptionType getType() {
        return type;
    }

    public void setType(SubscriptionType type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subscription)) return false;
        Subscription that = (Subscription) o;
        return Float.compare(that.getPrice(), getPrice()) == 0 && getDuration() == that.getDuration() && getType() == that.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getPrice(), getDuration());
    }

    @Override
    public String toString() {
        return "Subscription{" +
                super.toString() +
                ", Type=" + type +
                ", Price=" + price +
                ", Duration=" + duration +
                '}';
    }
}
