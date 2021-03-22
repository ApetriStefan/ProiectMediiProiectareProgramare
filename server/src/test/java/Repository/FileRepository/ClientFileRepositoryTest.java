package Repository.FileRepository;

import intrusii.server.Domain.Client;
import intrusii.server.Domain.Validators.ClientValidator;
import intrusii.server.Domain.Validators.ContractException;
import intrusii.server.Repository.FileRepository.ClientFileRepository;
import intrusii.server.Repository.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ClientFileRepositoryTest {
    private static final Client c1 = new Client("123", "name1", "email1@", "address1");
    private static final Client c2 = new Client("234", "name2", "email2@", "address2");
    private static final Client c3 = null;
    private Repository<Long, Client> repo;

    @Before
    public void setUp() throws Exception {
        c1.setId(1L);
        c2.setId(5L);
        ClientValidator validator = new ClientValidator();
        repo = new ClientFileRepository(validator, "resources/data/File/SubscriptionsFileTest");
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
        Set<Client> sc = new HashSet<>();
        sc.add(c1);
        assertEquals("Clients should be equal", sc, repo.findAll());
    }

    @Test
    public void testSave() throws Exception {
        repo.save(c2);
        Client c = repo.findOne(5L).get();
        assertEquals("Clients should be equal", c2, c);
    }

    @Test(expected = NullPointerException.class)
    public void testSaveException() throws Exception {
        repo.save(c3);
    }

    @Test
    public void testUpdate() throws Exception {
        c1.setCnp("345");
        c1.setEmail("email3@");
        c1.setName("name3");
        c1.setAddress("address3");
        repo.update(c1);
        Client c = repo.findOne(1L).get();
        assertEquals("Clients should be equal", c1, c);
        c1.setCnp("123");
        c1.setEmail("email1@");
        c1.setName("name1");
        c1.setAddress("address1");
        repo.update(c1);
    }

    @Test
    public void testDelete() throws Exception {
        assertEquals("Clients should be equal", c2, repo.delete(c2.getId()).get());
    }

    @Test(expected = ContractException.class)
    public void testUpdateException() throws Exception {
        repo.update(c3);
    }
}