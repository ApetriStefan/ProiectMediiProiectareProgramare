package Domain;

import intrusii.common.Domain.Contract;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;


public class ContractTest {
    private static final Long ID = 1L;
    private static final Long NEW_ID = 2L;
    private static final Long CLIENT_ID = 1L;
    private static final Long NEW_CLIENT_ID = 2L;
    private static final Long SUBSCRIPTION_ID = 1L;
    private static final Long NEW_SUBSCRIPTION_ID = 2L;
    private static final LocalDate DATE = LocalDate.of(2021, 5, 3);
    private static final LocalDate NEW_DATE = LocalDate.of(2022, 7, 23);

    private Contract contract;

    @Before
    public void setUp() throws Exception {
        contract = new Contract(CLIENT_ID,SUBSCRIPTION_ID, DATE);
        contract.setId(ID);
    }

    @After
    public void tearDown() throws Exception {
        contract=null;
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals("Ids should be equal", ID, contract.getId());
    }

    @Test
    public void testSetId() throws Exception {
        contract.setId(NEW_ID);
        assertEquals("Ids should be equal", NEW_ID, contract.getId());
    }

    @Test
    public void testGetClientID() throws Exception {
        assertEquals("Client IDs should be equal", CLIENT_ID, contract.getClientId());
    }

    @Test
    public void testSetClientID() throws Exception {
        contract.setClientId(NEW_CLIENT_ID);
        assertEquals("Client IDs should be equal", NEW_CLIENT_ID, contract.getClientId());
    }

    @Test
    public void testGetSubscriptionID() throws Exception {
        assertEquals("Subscription IDs should be equal", SUBSCRIPTION_ID, contract.getSubscriptionId());
    }
    @Test
    public void testSetSubscriptionID() throws Exception {
        contract.setSubscriptionId(NEW_SUBSCRIPTION_ID);
        assertEquals("Subscription IDs should be equal", NEW_SUBSCRIPTION_ID, contract.getSubscriptionId());
    }

    @Test
    public void testGetDate() throws Exception {
        assertEquals("Dates should be equal", DATE, contract.getDate());
    }
    @Test
    public void testSetDate() throws Exception {
        contract.setDate(NEW_DATE);
        assertEquals("Dates should be equal", NEW_DATE, contract.getDate());
    }
}