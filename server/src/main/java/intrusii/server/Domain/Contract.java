package intrusii.server.Domain;

import java.time.LocalDate;
import java.util.Objects;

public class Contract extends BaseEntity<Long> {
    private Long clientId;
    private Long subscriptionId;
    private LocalDate date;

    public Contract() {
    }

    public Contract(Long clientId, Long subscriptionId, LocalDate date) {
        this.clientId = clientId;
        this.subscriptionId = subscriptionId;
        this.date = date;
    }

    public Contract(Long clientId){
        this.clientId = clientId;
        this.subscriptionId = 0L;
        this.date = LocalDate.of(2000, 1, 1);
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contract)) return false;
        Contract contract = (Contract) o;
        return getClientId().equals(contract.getClientId()) && getSubscriptionId().equals(contract.getSubscriptionId()) && getDate().equals(contract.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClientId(), getSubscriptionId(), getDate());
    }

    @Override
    public String toString() {
        return "Contract{" +
                "Client ID=" + clientId +
                ", Subscription ID=" + subscriptionId +
                ", Date=" + date +
                '}' + super.toString();
    }
}
