package intrusii.server.Repository.DBRepository;

import intrusii.common.Domain.Contract;
import intrusii.common.Domain.Validators.ContractException;
import intrusii.common.Domain.Validators.Validator;
import intrusii.server.Repository.Repository;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ContractDBRepository implements Repository<Long, Contract> {
    private final String url = "";
    private final String user = "";
    private final String password = "";
    private final Validator<Contract> validator;

    public ContractDBRepository(Validator<Contract> validator) {
        this.validator = validator;
    }

    @Override
    public Optional<Contract> findOne(Long id){
        if(id == null){
            throw new IllegalArgumentException("Id must not be null");
        }

        String sql = "SELECT * FROM Contract WHERE id = ?";
        Contract contract = null;
        try(var connection = DriverManager.getConnection(url, user, password);
            var ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try(var rs = ps.executeQuery()){
                if(rs.next()) {
                    Long clientId = rs.getLong("clientId");
                    Long subscriptionId = rs.getLong("subscriptionId");
                    LocalDate date = rs.getDate("date").toLocalDate();
                    contract = new Contract(clientId, subscriptionId, date);
                    contract.setId(id);
                }
            }catch (SQLException ex){
                throw new ContractException(ex);
            }
        }catch (SQLException ex){
            throw new ContractException(ex);
        }
        return Optional.ofNullable(contract);
    }

    @Override
    public Iterable<Contract> findAll() {
        Set<Contract> allContracts = new HashSet<>();
        String sql = "SELECT * FROM Contract";

        try(var connection = DriverManager.getConnection(url, user, password);
            var ps = connection.prepareStatement(sql);
            var rs = ps.executeQuery()){
            while(rs.next()){
                Long id = rs.getLong("id");
                Long clientId = rs.getLong("clientId");
                Long subscriptionId = rs.getLong("subscriptionId");
                LocalDate date = rs.getDate("date").toLocalDate();
                Contract contract = new Contract(clientId, subscriptionId, date);
                contract.setId(id);

                allContracts.add(contract);
            }
        }catch (SQLException ex){
            throw new ContractException(ex);
        }
        return allContracts;
    }

    @Override
    public Optional<Contract> save(Contract contract){
        validator.validate(contract);

        String sql = "INSERT INTO Contract (clientId, subscriptionId, date) VALUES (?, ?, ?)";
        try (var connection = DriverManager.getConnection(url, user, password);
             var ps = connection.prepareStatement(sql)) {
            ps.setLong(1, contract.getClientId());
            ps.setLong(2, contract.getSubscriptionId());
            ps.setDate(3, Date.valueOf(contract.getDate()));

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new ContractException(ex);
        }
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
        try (var connection = DriverManager.getConnection(url, user, password);
             var ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new ContractException(ex);
        }
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
        try (var connection = DriverManager.getConnection(url, user, password);
             var ps = connection.prepareStatement(sql)) {
            ps.setLong(1, contract.getClientId());
            ps.setLong(2, contract.getSubscriptionId());
            ps.setDate(3, Date.valueOf(contract.getDate()));
            ps.setLong(4, contract.getId());

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new ContractException(ex);
        }

        return Optional.of(contract);
    }
}
