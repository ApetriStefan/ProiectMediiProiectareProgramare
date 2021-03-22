package Repository.XMLRepository;

import intrusii.server.Domain.Subscription;
import intrusii.server.Domain.SubscriptionType;
import intrusii.server.Domain.Validators.SubscriptionValidator;
import intrusii.server.Repository.Repository;
import intrusii.server.Repository.XMLRepository.SubscriptionXMLRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SubscriptionXMLRepositoryTest
{

    private static Subscription s1 = new Subscription(SubscriptionType.valueOf("Internet"),40.0F,24);
    private static Subscription s2 = new Subscription(SubscriptionType.valueOf("TV"),39.99F,24);
    private static Subscription s3 = null;
    private Repository<Long, Subscription> repo;

    @Before
    public void setUp() throws Exception {
        s1.setId(1L);
        s2.setId(2L);
        SubscriptionValidator validator = new SubscriptionValidator();
        repo = new SubscriptionXMLRepository(validator, "./data/Tests/SubscriptionsXMLTest");
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
        Set<Subscription> sc = new HashSet<Subscription>();
        sc.add(s1);
        //Iterator<Subscription> i = sc.iterator();
        assertEquals("Subscriptions should be equal", sc, repo.findAll());
    }

    @Test
    public void testSave() throws Exception {
        repo.save(s2);
        Subscription c = repo.findOne(4L).get();
        assertEquals("Subscriptions should be equal", s2, c);
        repo.delete(s2.getId());
    }

    @Test(expected = NullPointerException.class)
    public void testSaveException() throws Exception {
        repo.save(s3);
    }
    @Test
    public void testDelete() throws Exception
    {
        s2.setId(4L);
        repo.save(s2);
        assertEquals("Subscriptions should be equal", s2, repo.delete(s2.getId()).get());
    }
}
