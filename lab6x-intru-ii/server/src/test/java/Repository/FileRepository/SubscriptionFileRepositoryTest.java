package Repository.FileRepository;

import intrusii.common.Domain.Subscription;
import intrusii.common.Domain.SubscriptionType;
import intrusii.common.Domain.Validators.ContractException;
import intrusii.common.Domain.Validators.SubscriptionValidator;
import intrusii.server.Repository.FileRepository.SubscriptionFileRepository;
import intrusii.server.Repository.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SubscriptionFileRepositoryTest {
    private static final Subscription s1 = new Subscription(SubscriptionType.TV, 10, 10);
    private static final Subscription s2 = new Subscription(SubscriptionType.Phone, 11, 11);
    private static final Subscription s3 = null;
    private Repository<Long, Subscription> repo;

    @Before
    public void setUp() throws Exception {
        s1.setId(1L);
        s2.setId(5L);
        SubscriptionValidator validator = new SubscriptionValidator();
        repo = new SubscriptionFileRepository(validator, "/data/Tests/File/SubscriptionsFileTest");
    }

    @After
    public void tearDown() throws Exception {
        repo = null;
    }

    @Test
    public void testFindOne() throws Exception {
        assertEquals("Subscriptions should be equal", s1, repo.findOne(1L).get());
    }

    @Test
    public void testFindAll() throws Exception {
        Set<Subscription> ss = new HashSet<>();
        ss.add(s1);
        assertEquals("Subscriptions should be equal", ss, repo.findAll());
    }

    @Test
    public void testSave() throws Exception {
        repo.save(s2);
        Subscription s = repo.findOne(5L).get();
        assertEquals("Contracts should be equal", s2, s);
    }

    @Test(expected = NullPointerException.class)
    public void testSaveException() throws Exception {
        repo.save(s3);
    }

    @Test
    public void testUpdate() throws Exception {
        s1.setDuration(12);
        s1.setPrice(12);
        s1.setType(SubscriptionType.Phone);
        repo.update(s1);
        Subscription s = repo.findOne(1L).get();
        assertEquals("Subscriptions should be equal", s1, s);
        s1.setDuration(10);
        s1.setPrice(10);
        s1.setType(SubscriptionType.TV);
        repo.update(s1);
    }

    @Test
    public void testDelete() throws Exception {
        assertEquals("Subscriptions should be equal", s2, repo.delete(s2.getId()).get());
    }

    @Test(expected = ContractException.class)
    public void testUpdateException() throws Exception {
        repo.update(s3);
    }
}
