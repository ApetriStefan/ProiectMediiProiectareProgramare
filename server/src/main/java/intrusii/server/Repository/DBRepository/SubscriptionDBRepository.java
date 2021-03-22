package intrusii.server.Repository.DBRepository;

import intrusii.server.Domain.Subscription;
import intrusii.server.Domain.SubscriptionType;
import intrusii.server.Domain.Validators.ContractException;
import intrusii.server.Domain.Validators.Validator;
import intrusii.server.Repository.Repository;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SubscriptionDBRepository implements Repository<Long, Subscription> {
    private final String url;
    private final String user;
    private final String password;
    private final Validator<Subscription> validator;

    public SubscriptionDBRepository(Validator<Subscription> validator, String url, String user, String password) {
        this.validator = validator;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Optional<Subscription> findOne(Long id){
        if(id == null){
            throw new IllegalArgumentException("id must not be null");
        }

        String sql = "SELECT * FROM Subscription WHERE id = ?";
        Subscription subscription = null;
        try(var connection = DriverManager.getConnection(url, user, password);
            var ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try(var rs = ps.executeQuery()){
                if(rs.next()) {
                    SubscriptionType type = SubscriptionType.Default;
                    String typeString = rs.getString("type");
                    type = type.setSubscriptionType(typeString);
                    float price = rs.getFloat("price");
                    int duration = rs.getInt("duration");
                    subscription = new Subscription(type, price, duration);
                    subscription.setId(id);
                }
            }catch (SQLException ex){
                throw new ContractException(ex);
            }
        }catch (SQLException ex){
            throw new ContractException(ex);
        }
        return Optional.ofNullable(subscription);
    }

    @Override
    public Iterable<Subscription> findAll() {
        Set<Subscription> allSubscriptions = new HashSet<>();
        String sql = "SELECT * FROM Subscription";

        try(var connection = DriverManager.getConnection(url, user, password);
            var ps = connection.prepareStatement(sql);
            var rs = ps.executeQuery()){
            while(rs.next()){
                Long id = rs.getLong("id");
                SubscriptionType type = SubscriptionType.Default;
                String typeString = rs.getString("type");
                type = type.setSubscriptionType(typeString);
                float price = rs.getFloat("price");
                int duration = rs.getInt("duration");
                Subscription subscription = new Subscription(type, price, duration);
                subscription.setId(id);

                allSubscriptions.add(subscription);
            }
        }catch (SQLException ex){
            throw new ContractException(ex);
        }
        return allSubscriptions;
    }

    @Override
    public Optional<Subscription> save(Subscription subscription){
        validator.validate(subscription);

        String sql = "INSERT INTO Subscription (type, price, duration) VALUES (?, ?, ?)";
        try (var connection = DriverManager.getConnection(url, user, password);
             var ps = connection.prepareStatement(sql)) {
            ps.setString(1, subscription.getType().toString());
            ps.setFloat(2, subscription.getPrice());
            ps.setInt(3, subscription.getDuration());

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new ContractException(ex);
        }
        return Optional.of(subscription);
    }

    @Override
    public Optional<Subscription> delete(Long id){
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }

        findOne(id).orElseThrow(() -> new ContractException("No subscription with this id"));

        Optional<Subscription> subscription = findOne(id);
        String sql = "DELETE FROM Subscription WHERE id = ?";
        try (var connection = DriverManager.getConnection(url, user, password);
             var ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new ContractException(ex);
        }
        return subscription;
    }

    @Override
    public Optional<Subscription> update(Subscription subscription){
        if (subscription == null) {
            throw new IllegalArgumentException("subscription must not be null");
        }

        findOne(subscription.getId()).orElseThrow(() -> new ContractException("No subscription with this id"));

        String sql = "UPDATE Subscription SET type = ? , price = ?, duration = ? where id = ?";
        try (var connection = DriverManager.getConnection(url, user, password);
             var ps = connection.prepareStatement(sql)) {
            ps.setString(1, subscription.getType().toString());
            ps.setFloat(2, subscription.getPrice());
            ps.setInt(3, subscription.getDuration());
            ps.setLong(4, subscription.getId());

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new ContractException(ex);
        }

        return Optional.of(subscription);
    }
}
