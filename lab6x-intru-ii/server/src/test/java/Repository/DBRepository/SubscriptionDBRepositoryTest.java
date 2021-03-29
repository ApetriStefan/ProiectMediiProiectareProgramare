package Repository.DBRepository;

import intrusii.common.Domain.Subscription;
import intrusii.common.Domain.SubscriptionType;
import intrusii.common.Domain.Validators.ContractException;
import intrusii.common.Domain.Validators.SubscriptionValidator;
import intrusii.server.Repository.DBRepository.SubscriptionDBRepository;
import intrusii.server.Repository.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SubscriptionDBRepositoryTest {
    private Repository<Long, Subscription> repository;

    Subscription s1 = new Subscription(SubscriptionType.Internet, 10, 12);
    Subscription s2 = new Subscription(SubscriptionType.TV, 20, 24);
    Subscription s3 = new Subscription(SubscriptionType.Phone, 30, 48);

    @Before
    public void setUp() throws Exception {
        SubscriptionValidator validator = new SubscriptionValidator();
        repository = new SubscriptionDBRepository(validator);

        String sql = "ALTER SEQUENCE subscription_id_seq RESTART WITH 4";
        try (var connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/intrusiiJDBCTest", "postgres", "incorrect");
             var ps = connection.prepareStatement(sql)) {
            ps.executeQuery();
        }catch (SQLException ex){
            System.out.println("");
        }

        s1.setId(1L);
        s2.setId(2L);
        s3.setId(3L);
    }

    @After
    public void tearDown() throws Exception {
        repository = null;
    }

    @Test
    public void findOneTest() throws Exception{
        assertEquals("Subscriptions should be equal", s2, repository.findOne(2L).get());
    }

    @Test
    public void findAllTest() throws Exception{
        Set<Subscription> subscriptions = new HashSet<>();
        subscriptions.add(s1);
        subscriptions.add(s2);
        subscriptions.add(s3);
        assertEquals("The sets should be equal", repository.findAll(), subscriptions);
    }

    @Test
    public void saveTest() throws Exception {
        Subscription s4 = new Subscription(SubscriptionType.TV, 40, 80);
        s4.setId(4L);
        repository.save(s4);
        assertEquals("s4 should be in the database", s4, repository.findOne(4L).get());
        repository.delete(4L);
    }

    @Test
    public void deleteTest() throws Exception {
        Set<Subscription> subscriptions = new HashSet<>();
        subscriptions.add(s1);
        subscriptions.add(s2);
        subscriptions.add(s3);
        Subscription s4 = new Subscription(SubscriptionType.TV, 40, 80);
        repository.save(s4);
        repository.delete(4L);
        assertEquals("The sets should be equal", repository.findAll(), subscriptions);
    }

    @Test(expected = ContractException.class)
    public void deleteExceptionTest() throws Exception {
        repository.delete(5L);
    }

    @Test
    public void updateTest() throws  Exception {
        Subscription s4 = new Subscription(SubscriptionType.TV, 40, 80);
        s4.setId(3L);
        repository.update(s4);
        Subscription updatedSubscription = repository.findOne(3L).get();
        assertEquals("Types", updatedSubscription.getType(), SubscriptionType.TV);
        assertEquals("Prices should be equal", updatedSubscription.getPrice(), Float.parseFloat("40"), 0.001);
        assertEquals("Durations should be equal", updatedSubscription.getDuration(), 80);

        repository.update(s3);
    }

    @Test(expected = ContractException.class)
    public void updateExceptionTest() throws Exception {
        Subscription s4 = new Subscription(SubscriptionType.TV, 40, 80);
        s4.setId(5L);
        repository.update(s4);
    }
}
