package intrusii.server.Domain;

import java.util.Objects;


public class Client extends BaseEntity<Long> {
    private String cnp;
    private String name;
    private String email;
    private String address;

    public Client(){
    }

    public Client(String cnp, String name, String email, String address) {
        this.cnp = cnp;
        this.name = name;
        this.email = email;
        this.address= address;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address=address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return getCnp().equals(client.getCnp()) && getName().equals(client.getName()) && getEmail().equals(client.getEmail()) && getAddress().equals(client.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCnp(), getName(), getEmail(), getAddress());
    }

    @Override
    public String toString() {
        return "Client{" +
                "CNP='" + cnp + '\'' +
                ", name='" + name + '\'' +
                ", email=" + email + '\''+
                ", address=" + address+
                "} " + super.toString();
    }
}
