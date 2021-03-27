package intrusii.server.Repository.DBRepository;

import intrusii.server.Domain.Client;
import intrusii.server.Domain.Validators.ContractException;
import intrusii.server.Domain.Validators.Validator;
import intrusii.server.Repository.Repository;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ClientDBRepository implements Repository<Long, Client> {
    private final String url;
    private final String user;
    private final String password;
    private final Validator<Client> validator;

    public ClientDBRepository(Validator<Client> validator, String url, String user, String password) {
        this.validator = validator;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Optional<Client> findOne(Long id){
        if(id == null){
            throw new IllegalArgumentException("Id must not be null");
        }

        String sql = "SELECT * FROM Client WHERE id = ?";
        Client client = null;
        try(var connection = DriverManager.getConnection(url, user, password);
            var ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try(var rs = ps.executeQuery()){
                if(rs.next()) {
                    String cnp = rs.getString("cnp");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String address = rs.getString("address");
                    client = new Client(cnp, name, email, address);
                    client.setId(id);
                }
            }catch (SQLException ex){
                throw new ContractException(ex);
            }
        }catch (SQLException ex){
            throw new ContractException(ex);
        }
        return Optional.ofNullable(client);
    }

    @Override
    public Iterable<Client> findAll() {
        Set<Client> allClients = new HashSet<>();
        String sql = "SELECT * FROM Client";

        try(var connection = DriverManager.getConnection(url, user, password);
            var ps = connection.prepareStatement(sql);
            var rs = ps.executeQuery()){
            while(rs.next()){
                Long id = rs.getLong("id");
                String cnp = rs.getString("cnp");
                String name = rs.getString("name");
                String mail = rs.getString("email");
                String address = rs.getString("address");
                Client client = new Client(cnp, name, mail, address);
                client.setId(id);

                allClients.add(client);
            }
        }catch (SQLException ex){
            throw new ContractException(ex);
        }
        return allClients;
    }

    @Override
    public Optional<Client> save(Client client){
        validator.validate(client);

        String sql = "INSERT INTO Client (cnp, name, email, address) VALUES (?, ?, ?, ?)";
        try (var connection = DriverManager.getConnection(url, user, password);
             var ps = connection.prepareStatement(sql)) {
            ps.setString(1, client.getCnp());
            ps.setString(2, client.getName());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getAddress());

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new ContractException(ex);
        }
        return Optional.of(client);
    }

    @Override
    public Optional<Client> delete(Long id){
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }

        findOne(id).orElseThrow(() -> new ContractException("No client with this id"));

        Optional<Client> client = findOne(id);
        String sql = "DELETE FROM Client WHERE id = ?";
        try (var connection = DriverManager.getConnection(url, user, password);
             var ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new ContractException(ex);
        }
        return client;
    }

    @Override
    public Optional<Client> update(Client client){
        if (client == null) {
            throw new IllegalArgumentException("Client must not be null");
        }

        findOne(client.getId()).orElseThrow(() -> new ContractException("No client with this id"));

        validator.validate(client);

        String sql = "UPDATE Client SET cnp = ? , name = ?, email = ?, address = ? WHERE id = ?";
        try (var connection = DriverManager.getConnection(url, user, password);
             var ps = connection.prepareStatement(sql)) {
            ps.setString(1, client.getCnp());
            ps.setString(2, client.getName());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getAddress());
            ps.setLong(5, client.getId());

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new ContractException(ex);
        }

        return Optional.of(client);
    }
}
