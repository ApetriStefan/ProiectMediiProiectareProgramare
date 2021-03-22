package intrusii.server.Domain;

public class BaseEntity<ID> {
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "Domain.BaseEntity{" + "id=" + id + '}';
    }
}
