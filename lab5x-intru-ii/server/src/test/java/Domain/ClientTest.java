package Domain;

import intrusii.server.Domain.Client;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClientTest {
    private static final Long ID = 1L;
    private static final Long NEW_ID = 1L;
    private static final String CNP = "5000323330204";
    private static final String NEW_CNP = "6001213330215";
    private static final String NAME = "Mike";
    private static final String NEW_NAME = "Michael";
    private static final String EMAIL = "mike@gmail.com";
    private static final String NEW_EMAIL = "michael@gmail.com";
    private static final String ADDRESS = "Str. Please, Nr 56";
    private static final String NEW_ADDRESS = "Str. Work, Nr 35";

    private Client client;

    @Before
    public void setUp() throws Exception {
        client = new Client(CNP, NAME, EMAIL, ADDRESS);
        client.setId(ID);
    }

    @After
    public void tearDown() throws  Exception {
        client = null;
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals( "Ids should be equal", ID, client.getId());
    }

    @Test
    public void testSetId() throws Exception {
        client.setId(NEW_ID);
        assertEquals("Ids should be equal", NEW_ID, client.getId());
    }

    @Test
    public void testGetCnp() throws Exception {
        assertEquals( "CNP should be equal", CNP, client.getCnp());
    }

    @Test
    public void testSetCnp() throws Exception {
        client.setCnp(NEW_CNP);
        assertEquals("CNP should be equal", NEW_CNP, client.getCnp());
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("Names should be equal", NAME, client.getName());
    }

    @Test
    public void testSetName() throws Exception {
        client.setName(NEW_NAME);
        assertEquals("Names should be equal", NEW_NAME, client.getName());
    }

    @Test
    public void testGetEmail() throws Exception {
        assertEquals("Email should be equal", EMAIL, client.getEmail());
    }

    @Test
    public void testSetEmail() throws Exception {
        client.setEmail(NEW_EMAIL);
        assertEquals("Emails should be equal", NEW_EMAIL, client.getEmail());
    }

    @Test
    public void testGetAddress() throws Exception {
        assertEquals("Addresses should be equal", ADDRESS, client.getAddress());
    }

    @Test
    public void testSetAddress() throws Exception {
        client.setAddress(NEW_ADDRESS);
        assertEquals("Addresses should be equal", NEW_ADDRESS, client.getAddress());
    }
}