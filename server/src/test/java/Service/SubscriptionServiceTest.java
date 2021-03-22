package Service;

import intrusii.server.Domain.Subscription;
import intrusii.server.Domain.SubscriptionType;
import intrusii.server.Domain.Validators.SubscriptionValidator;
import intrusii.server.Repository.FileRepository.SubscriptionFileRepository;
import intrusii.server.Repository.Repository;
import intrusii.server.Service.SubscriptionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SubscriptionServiceTest {
    private SubscriptionValidator subscriptionValidator;
    private Repository<Long, Subscription> subscriptionRepository;
    private SubscriptionService subscriptionService;
    private static final Subscription c1 = new Subscription(SubscriptionType.TV, 10, 10);
    private static final Subscription c2 = new Subscription(SubscriptionType.Phone, 11, 11);
    private static final Subscription c3 = new Subscription(SubscriptionType.Internet, 12, 12);

    @Before
    public void setUp() throws Exception {
        c1.setId(1L);
        c2.setId(2L);
        subscriptionValidator = new SubscriptionValidator();
        subscriptionRepository = new SubscriptionFileRepository(subscriptionValidator, "resources/data/ServiceFile/SubscriptionsServiceTest");
        subscriptionService = new SubscriptionService(subscriptionRepository);
    }

    @After
    public void tearDown() throws Exception {
        subscriptionService = null;
        subscriptionRepository = null;
        subscriptionValidator = null;
    }

    @Test
    public void testGetAllSubscriptions() {
        Set<Subscription> subscriptionSet = new HashSet<>();
        subscriptionSet.add(c1);
        subscriptionSet.add(c2);
        assertEquals("Subscriptions should be equal", subscriptionSet, subscriptionService.getAllSubscriptions());
    }

    @Test
    public void testAddSubscription() throws Exception {
        subscriptionService.addSubscription(c3);
        assertEquals("Subscriptions should be equal", c3, subscriptionRepository.findOne(c3.getId()).get());
        subscriptionService.deleteSubscription(c3.getId());
    }

    @Test
    public void testDeleteSubscription() throws Exception {
        subscriptionService.addSubscription(c3);
        subscriptionService.deleteSubscription(c3.getId());
        Set<Subscription> subscriptionSet = new HashSet<>();
        subscriptionSet.add(c1);
        subscriptionSet.add(c2);
        assertEquals("Subscriptions should be equal", subscriptionSet, subscriptionService.getAllSubscriptions());
    }

    @Test
    public void testUpdateSubscription() throws Exception {
        c3.setId(c2.getId());
        subscriptionService.updateSubscription(c3);
        assert subscriptionService.getSubscriptionByID(c3.getId()).equals(c3) : "The new Subscription should be present";
        subscriptionService.updateSubscription(c2);
    }

    @Test
    public void testGetSubscriptionById() throws Exception {
        assert subscriptionService.getSubscriptionByID(1L).equals(c1) : "Subscriptions should be equal";
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSubscriptionByIdException() throws Exception {
        subscriptionService.getSubscriptionByID(5L);
    }

    @Test
    public void testFilterByDuration() {
        assert subscriptionService.filterByDuration(10).get(0).equals(c1) : "Subscriptions should be equal";
    }

    @Test
    public void testFilterByType() {
        assert subscriptionService.filterByType("Phone").get(0).equals(c2) : "Subscriptions should be equal";
    }
}
