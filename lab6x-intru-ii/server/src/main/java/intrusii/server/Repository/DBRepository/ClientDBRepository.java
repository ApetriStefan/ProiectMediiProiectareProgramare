package intrusii.server.Repository.DBRepository;

import intrusii.common.Domain.Client;
import intrusii.common.Domain.Validators.ClientValidator;
import intrusii.common.Domain.Validators.ContractException;
import intrusii.common.Domain.Validators.Validator;
import intrusii.server.Repository.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;

import javax.swing.text.html.Option;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ClientDBRepository implements Repository<Long, Client> {
    @Autowired
    private JdbcOperations jdbcOperations;

    private Validator<Client> validator;

    public ClientDBRepository(Validator<Client> clientValidator) {
        this.validator=clientValidator;
    }

    @Override
    public Optional<Client> findOne(Long id){
        if(id == null){
            throw new IllegalArgumentException("Id must not be null");
        }

        String sql = "SELECT * FROM Client WHERE id = ?";
        Client clientTemp;
        clientTemp = jdbcOperations.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new Client(
                        rs.getString("cnp"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("address")
                ));
        clientTemp.setId(id);
        return Optional.ofNullable(clientTemp);

//        Client client = jdbcOperations.query(sql, rs -> {
//            String cnp = rs.getString("cnp");
//            String name = rs.getString("name");
//            String email = rs.getString("email");
//            String address = rs.getString("address");
//            Client c = new Client(cnp, name, email, address);
//            c.setId(id);
//            return c;
//        });
//        client.setId(id);
//        return Optional.ofNullable(client);
    }

    @Override
    public Iterable<Client> findAll() {
        String sql = "SELECT * FROM Client";
        return jdbcOperations.query(sql, (rs, i) -> {
            Long cid = rs.getLong("id");
            String cnp = rs.getString("cnp");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String address = rs.getString("address");
            Client client = new Client(cnp, name, email, address);
            client.setId(cid);
            return client;
        });
    }

    @Override
    public Optional<Client> save(Client client){
        validator.validate(client);

        String sql = "INSERT INTO Client (cnp, name, email, address) VALUES (?, ?, ?, ?)";
        jdbcOperations.update(sql, client.getCnp(), client.getName(), client.getEmail(), client.getAddress());
        return Optional.of(client);
    }

    @Override
    public Optional<Client> delete(Long id){
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }


        findOne(id).orElseThrow(() -> new ContractException("No client with this id"));


        String sql = "DELETE FROM Client WHERE id = ?";


        Optional<Client> client = findOne(id);
        jdbcOperations.update(sql, id);
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
        jdbcOperations.update(sql, client.getCnp(), client.getName(), client.getEmail(), client.getAddress(), client.getId());
        return Optional.of(client);
    }
}
