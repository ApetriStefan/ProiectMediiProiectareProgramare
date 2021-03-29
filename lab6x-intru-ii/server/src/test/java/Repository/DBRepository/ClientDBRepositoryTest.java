package Repository.DBRepository;

import intrusii.common.Domain.Client;
import intrusii.common.Domain.Validators.ClientValidator;
import intrusii.common.Domain.Validators.ContractException;
import intrusii.server.Repository.DBRepository.ClientDBRepository;
import intrusii.server.Repository.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ClientDBRepositoryTest {
    private Repository<Long, Client> repository;

    Client c1 = new Client("1", "Name1", "email1@", "Address1");
    Client c2 = new Client("2", "Name2", "email2@", "Address2");
    Client c3 = new Client("3", "Name3", "email3@", "Address3");

    @Before
    public void setUp() throws Exception {
        ClientValidator validator = new ClientValidator();
        repository = new ClientDBRepository(validator);

        String sql = "ALTER SEQUENCE client_id_seq RESTART WITH 4";
        try (var connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/intrusiiJDBCTest", "postgres", "cfsa123");
             var ps = connection.prepareStatement(sql)) {
            ps.executeQuery();
        }catch (SQLException ex){
            System.out.println("");
        }

        c1.setId(1L);
        c2.setId(2L);
        c3.setId(3L);
    }

    @After
    public void tearDown() throws Exception {
        repository = null;
    }

    @Test
    public void findOneTest() throws Exception{
        assertEquals("Clients should be equal", c2, repository.findOne(2L).get());
    }

    @Test
    public void findAllTest() throws Exception{
        Set<Client> clients = new HashSet<>();
        clients.add(c1);
        clients.add(c2);
        clients.add(c3);
        assertEquals("The sets should be equal", repository.findAll(), clients);
    }

    @Test
    public void saveTest() throws Exception {
        Client c4 = new Client("4", "Name4", "email4@", "Address4");
        c4.setId(4L);
        repository.save(c4);
        assertEquals("c4 should be in the database", c4, repository.findOne(4L).get());
        repository.delete(4L);
    }

    @Test
    public void deleteTest() throws Exception {
        Set<Client> clients = new HashSet<>();
        clients.add(c1);
        clients.add(c2);
        clients.add(c3);
        Client c4 = new Client("4", "Name4", "email4@", "Address4");
        repository.save(c4);
        repository.delete(4L);
        assertEquals("The sets should be equal", repository.findAll(), clients);
    }

    @Test(expected = ContractException.class)
    public void deleteExceptionTest() throws Exception {
        repository.delete(5L);
    }

    @Test
    public void updateTest() throws  Exception {
        Client c4 = new Client("4", "Name4", "email4@", "Address4");
        c4.setId(3L);
        repository.update(c4);
        Client updatedClient = repository.findOne(3L).get();
        assertEquals("CNPs should be equal", updatedClient.getCnp(), "4");
        assertEquals("Names should be equal", updatedClient.getName(),"Name4");
        assertEquals("Emails should be equal", updatedClient.getEmail(), "email4@");
        assertEquals("Addresses should be equal", updatedClient.getAddress(), "Address4");

        repository.update(c3);
    }

    @Test(expected = ContractException.class)
    public void updateExceptionTest() throws Exception {
        Client c4 = new Client("4", "Name4", "email4@", "Address4");
        c4.setId(5L);
        repository.update(c4);
    }
}
