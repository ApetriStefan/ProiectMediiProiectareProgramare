package intrusii.server.Repository.DBRepository;

import intrusii.common.Domain.Client;
import intrusii.common.Domain.Contract;
import intrusii.common.Domain.Validators.ContractException;
import intrusii.common.Domain.Validators.ContractValidator;
import intrusii.common.Domain.Validators.Validator;
import intrusii.common.Domain.Validators.ValidatorException;
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

    private Validator<Contract> validator;

    public ContractDBRepository(Validator<Contract> contractValidator)
    {
        this.validator = contractValidator;
    }

    @Override
    public Optional<Contract> findOne(Long id){
        if(id == null){
            throw new ValidatorException("Id must not be null");
        }

        String sql = "SELECT * FROM Contract WHERE id = ?";
        Contract contractTemp;
        contractTemp = jdbcOperations.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new Contract(
                        rs.getLong("clientId"),
                        rs.getLong("subscriptionId"),
                        rs.getDate("date").toLocalDate()
                ));
        contractTemp.setId(id);
        return Optional.ofNullable(contractTemp);
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
            throw new ValidatorException("Id must not be null");
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
            throw new ValidatorException("Contract must not be null");
        }

        findOne(contract.getId()).orElseThrow(() -> new ContractException("No contract with this id"));
        validator.validate(contract);

        String sql = "UPDATE Contract SET clientId = ?, subscriptionId = ?, date = ? where id = ?";
        jdbcOperations.update(sql, contract.getClientId(), contract.getSubscriptionId(), contract.getDate(), contract.getId());
        return Optional.of(contract);
    }
}
