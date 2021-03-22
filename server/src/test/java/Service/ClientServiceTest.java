package Service;

import intrusii.server.Domain.Client;
import intrusii.server.Domain.Validators.ClientValidator;
import intrusii.server.Repository.FileRepository.ClientFileRepository;
import intrusii.server.Repository.Repository;
import intrusii.server.Service.ClientService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ClientServiceTest {
    private ClientValidator clientValidator;
    private Repository<Long, Client> clientRepository;
    private ClientService clientService;
    private static final Client c1 = new Client("123", "name1", "email1@", "address1");
    private static final Client c2 = new Client("456", "name2", "email2@", "address2");
    private static final Client c3 = new Client("789", "name3", "email3@", "address3");

    @Before
    public void setUp() throws Exception {
        c1.setId(1L);
        c2.setId(2L);
        clientValidator = new ClientValidator();
        clientRepository = new ClientFileRepository(clientValidator, "resources/data/ServiceFile/ClientsServiceTest");
        clientService = new ClientService(clientRepository);
    }

    @After
    public void tearDown() throws Exception {
        clientService = null;
        clientRepository = null;
        clientValidator = null;
    }

    @Test
    public void testGetAllClients() {
        Set<Client> clientSet = new HashSet<>();
        clientSet.add(c1);
        clientSet.add(c2);
        assertEquals("Clients should be equal", clientSet, clientService.getAllClients());
    }

    @Test
    public void testAddClient() throws Exception {
        clientService.addClient(c3);
        assertEquals("Clients should be equal", c3, clientRepository.findOne(c3.getId()).get());
        clientService.deleteClient(c3.getId());
    }

    @Test
    public void testDeleteClient() throws Exception {
        clientService.addClient(c3);
        clientService.deleteClient(c3.getId());
        Set<Client> clientSet = new HashSet<>();
        clientSet.add(c1);
        clientSet.add(c2);
        assertEquals("Clients should be equal", clientSet, clientService.getAllClients());
    }

    @Test
    public void testUpdateClient() throws Exception {
        c3.setId(c2.getId());
        clientService.updateClient(c3);
        assert clientService.getClientByID(c3.getId()).equals(c3) : "The new client should be present";
        clientService.updateClient(c2);
    }

    @Test
    public void testGetClientById() throws Exception {
        assert clientService.getClientByID(1L).equals(c1) : "clients should be equal";
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetClientByIdException() throws Exception {
        clientService.getClientByID(5L);
    }

    @Test
    public void testFilteredByClientName() {
        assert clientService.filteredByClientName("1").get(0).equals(c1);
    }

    @Test
    public void testFilteredByClientCNP() {
        assert clientService.filteredByClientCNP("123").get(0).equals(c1);
    }
}
