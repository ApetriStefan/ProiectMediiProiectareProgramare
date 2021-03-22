package Repository.XMLRepository;

import intrusii.server.Domain.Client;
import intrusii.server.Domain.Validators.ClientValidator;
import intrusii.server.Repository.Repository;
import intrusii.server.Repository.XMLRepository.ClientXMLRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ClientXMLRepositoryTest
{

    private static Client c1 = new Client("123", "name1", "email1@", "address1");
    private static Client c2 = new Client("123", "name2", "email2@", "address2");
    private static Client c3 = null;
    private Repository<Long, Client> repo;

    @Before
    public void setUp() throws Exception {
        c1.setId(1L);
        c2.setId(2L);
        ClientValidator validator = new ClientValidator();
        repo = new ClientXMLRepository(validator, "resources/data/XML/ClientsXMLTest");
    }
    @After
    public void tearDown() throws Exception {
        repo = null;
    }
    @Test
    public void testFindOne() throws Exception {
        assertEquals("Clients should be equal", c1, repo.findOne(1L).get());
    }
    @Test
    public void testFindAll() throws Exception {
        Set<Client> sc = new HashSet<Client>();
        sc.add(c1);
        //Iterator<Client> i = sc.iterator();
        assertEquals("Clients should be equal", sc, repo.findAll());
    }
    @Test
    public void testSave() throws Exception {
        repo.save(c2);
        Client c = repo.findOne(4L).get();
        assertEquals("Clients should be equal", c2, c);
        repo.delete(c2.getId());
    }
    @Test(expected = NullPointerException.class)
    public void testSaveException() throws Exception {
        repo.save(c3);
    }
    @Test
    public void testDelete() throws Exception
    {
        c2.setId(4L);
        repo.save(c2);
        assertEquals("Clients should be equal", c2, repo.delete(c2.getId()).get());
    }
}
