package intrusii.server.Repository.DBRepository;

import intrusii.common.Domain.Client;
import intrusii.common.Domain.Contract;
import intrusii.common.Domain.Validators.ContractException;
import intrusii.common.Domain.Validators.ContractValidator;
import intrusii.common.Domain.Validators.Validator;
import intrusii.server.Repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ContractDBRepository implements Repository<Long, Contract> {
    @Autowired
    private JdbcOperations jdbcOperations;

    private ContractValidator validator;

    @Override
    public Optional<Contract> findOne(Long id){
        if(id == null){
            throw new IllegalArgumentException("Id must not be null");
        }

        String sql = "SELECT * FROM Contract WHERE id = ?";
        Contract contract = jdbcOperations.query(sql, rs -> {
            Long clientId = rs.getLong("clientId");
            Long subscriptionId = rs.getLong("subscriptionId");
            LocalDate date = rs.getDate("date").toLocalDate();
            Contract c  = new Contract(clientId, subscriptionId, date);
            c.setId(id);
            return c;
        });
        return Optional.ofNullable(contract);
    }

    @Override
    public Iterable<Contract> findAll() {
        String sql = "SELECT * FROM Contract";
        return jdbcOperations.query(sql, (rs, i) -> {
            Long id = rs.getLong("id");
            Long clientId = rs.getLong("clientId");
            Long subscriptionId = rs.getLong("subscriptionId");
            LocalDate date = rs.getDate("date").toLocalDate();
            Contract contract = new Contract(clientId, subscriptionId, date);
            contract.setId(id);
            return contract;
        });
    }

    @Override
    public Optional<Contract> save(Contract contract){
        validator.validate(contract);

        String sql = "INSERT INTO Contract (clientId, subscriptionId, date) VALUES (?, ?, ?)";
        jdbcOperations.update(sql, contract.getClientId(), contract.getSubscriptionId(), contract.getDate());
        return Optional.of(contract);
    }

    @Override
    public Optional<Contract> delete(Long id){
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }

        findOne(id).orElseThrow(() -> new ContractException("No contract with this id"));

        Optional<Contract> contract = findOne(id);
        String sql = "DELETE FROM Contract WHERE id = ?";
        jdbcOperations.update(sql, id);
        return contract;
    }

    @Override
    public Optional<Contract> update(Contract contract){
        if (contract == null) {
            throw new IllegalArgumentException("Contract must not be null");
        }

        findOne(contract.getId()).orElseThrow(() -> new ContractException("No contract with this id"));
        validator.validate(contract);

        String sql = "UPDATE Contract SET clientId = ?, subscriptionId = ?, date = ? where id = ?";
        jdbcOperations.update(sql, contract.getClientId(), contract.getSubscriptionId(), contract.getDate(), contract.getId());
        return Optional.of(contract);
    }
}
