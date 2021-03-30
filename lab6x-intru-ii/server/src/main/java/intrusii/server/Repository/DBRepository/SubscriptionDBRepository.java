package intrusii.server.Repository.DBRepository;

import intrusii.common.Domain.Client;
import intrusii.common.Domain.Contract;
import intrusii.common.Domain.Subscription;
import intrusii.common.Domain.SubscriptionType;
import intrusii.common.Domain.Validators.ContractException;
import intrusii.common.Domain.Validators.SubscriptionValidator;
import intrusii.common.Domain.Validators.Validator;
import intrusii.server.Repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SubscriptionDBRepository implements Repository<Long, Subscription> {
    @Autowired
    private JdbcOperations jdbcOperations;

    private Validator<Subscription> validator;

    public SubscriptionDBRepository(Validator<Subscription> subscriptionValidator)
    {
        this.validator=subscriptionValidator;
    }

    @Override
    public Optional<Subscription> findOne(Long id){
        if(id == null){
            throw new IllegalArgumentException("Id must not be null");
        }

        String sql = "SELECT * FROM Subscription WHERE id = ?";
        Subscription subscriptionTemp;
        subscriptionTemp = jdbcOperations.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new Subscription(
                        SubscriptionType.valueOf(rs.getString("type")),
                        rs.getFloat("price"),
                        rs.getInt("duration")
                ));
        subscriptionTemp.setId(id);
        return Optional.ofNullable(subscriptionTemp);
    }

    @Override
    public Iterable<Subscription> findAll() {
        String sql = "SELECT * FROM Subscription";
        return jdbcOperations.query(sql, (rs, i) -> {
            Long id = rs.getLong("id");
            SubscriptionType type = SubscriptionType.Default;
            String typeString = rs.getString("type");
            type = type.setSubscriptionType(typeString);
            float price = rs.getFloat("price");
            int duration = rs.getInt("duration");
            Subscription subscription = new Subscription(type, price, duration);
            subscription.setId(id);
            return subscription;
        });
    }

    @Override
    public Optional<Subscription> save(Subscription subscription){
        validator.validate(subscription);

        String sql = "INSERT INTO Subscription (type, price, duration) VALUES (?, ?, ?)";
        jdbcOperations.update(sql, subscription.getType(), subscription.getPrice(), subscription.getDuration());
        return Optional.of(subscription);
    }

    @Override
    public Optional<Subscription> delete(Long id){
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }

        findOne(id).orElseThrow(() -> new ContractException("No subscription with this id"));

        Optional<Subscription> subscription = findOne(id);
        String sql = "DELETE FROM Subscription WHERE id = ?";
        jdbcOperations.update(sql, id);
        return subscription;
    }

    @Override
    public Optional<Subscription> update(Subscription subscription){
        if (subscription == null) {
            throw new IllegalArgumentException("Subscription must not be null");
        }

        findOne(subscription.getId()).orElseThrow(() -> new ContractException("No subscription with this id"));
        validator.validate(subscription);

        String sql = "UPDATE Subscription SET type = ? , price = ?, duration = ? where id = ?";
        jdbcOperations.update(sql, subscription.getType(), subscription.getPrice(), subscription.getDuration());
        return Optional.of(subscription);
    }
}
