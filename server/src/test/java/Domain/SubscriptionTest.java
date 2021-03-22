package Domain;

import intrusii.server.Domain.Subscription;
import intrusii.server.Domain.SubscriptionType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SubscriptionTest {
    private static final Long ID = 1L;
    private static final Long newID = 2L;
    private static final SubscriptionType type = SubscriptionType.TV;
    private static final SubscriptionType newType = SubscriptionType.Phone;
    private static final float price = 100;
    private static final float newPrice = 200;
    private static final int duration = 12;
    private static final int newDuration = 24;

    private static Subscription subscription;

    @Before
    public void setUp() throws Exception {
        subscription = new Subscription(type, price, duration);
        subscription.setId(ID);
    }

    @After
    public void tearDown() throws Exception {
        subscription = null;
    }

    @Test
    public void testGetSubscriptionID() throws Exception {
        assertEquals("Subscription IDs should be equal", ID, subscription.getId());
    }

    @Test
    public void testSetSubscriptionID() throws Exception {
        subscription.setId(newID);
        assertEquals("Subscription IDs should be equal", newID, subscription.getId());
    }

    @Test
    public void testGetSubscriptionType() throws Exception {
        assertEquals("Subscription types should be equal", type, subscription.getType());
    }

    @Test
    public void testSetSubscriptionType() throws Exception {
        subscription.setType(newType);
        assertEquals("Subscription types should be equal", newType, subscription.getType());
    }

    @Test
    public void testGetSubscriptionPrice() throws Exception {
        assertEquals("Subscription prices should be equal", price, subscription.getPrice(), 0);
    }

    @Test
    public void testSetSubscriptionPrice() throws Exception {
        subscription.setPrice(newPrice);
        assertEquals("Subscription prices should be equal", newPrice, subscription.getPrice(), 0);
    }

    @Test
    public void testGetSubscriptionDuration() throws Exception {
        assertEquals("Subscription durations should be equal", duration, subscription.getDuration());
    }

    @Test
    public void testSetSubscriptionDuration() throws Exception {
        subscription.setDuration(newDuration);
        assertEquals("Subscription durations should be equal", newDuration, subscription.getDuration());
    }
}
