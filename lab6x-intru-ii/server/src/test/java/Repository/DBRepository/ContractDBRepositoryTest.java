package Repository.DBRepository;

import intrusii.server.Domain.Contract;
import intrusii.server.Domain.Validators.ContractException;
import intrusii.server.Domain.Validators.ContractValidator;
import intrusii.server.Repository.DBRepository.ContractDBRepository;
import intrusii.server.Repository.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ContractDBRepositoryTest {
    private Repository<Long, Contract> repository;

    Contract c1 = new Contract(1L, 2L, LocalDate.of(2020, 10,1));
    Contract c2 = new Contract(2L, 3L, LocalDate.of(2020, 11,2));
    Contract c3 = new Contract(1L, 3L, LocalDate.of(2020, 12,3));

    @Before
    public void setUp() throws Exception {
        ContractValidator validator = new ContractValidator();
        repository = new ContractDBRepository(validator, "jdbc:postgresql://localhost:5432/intrusiiJDBCTest", "postgres", "incorrect");

        String sql = "ALTER SEQUENCE contract_id_seq RESTART WITH 4";
        try (var connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/intrusiiJDBCTest", "postgres", "incorrect");
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
        assertEquals("Contracts should be equal", c2, repository.findOne(2L).get());
    }

    @Test
    public void findAllTest() throws Exception{
        Set<Contract> contracts = new HashSet<>();
        contracts.add(c1);
        contracts.add(c2);
        contracts.add(c3);
        assertEquals("The sets should be equal", repository.findAll(), contracts);
    }

    @Test
    public void saveTest() throws Exception {
        Contract c4 = new Contract(3L, 1L, LocalDate.of(2021, 01, 01));
        c4.setId(4L);
        repository.save(c4);
        assertEquals("c4 should be in the database", c4, repository.findOne(4L).get());
        repository.delete(4L);
    }

    @Test
    public void deleteTest() throws Exception {
        Set<Contract> contracts = new HashSet<>();
        contracts.add(c1);
        contracts.add(c2);
        contracts.add(c3);
        Contract c4 = new Contract(3L, 1L, LocalDate.of(2021, 01, 01));
        repository.save(c4);
        repository.delete(4L);
        assertEquals("The sets should be equal", repository.findAll(), contracts);
    }

    @Test(expected = ContractException.class)
    public void deleteExceptionTest() throws Exception {
        repository.delete(5L);
    }

    @Test
    public void updateTest() throws  Exception {
        Contract c4 = new Contract(3L, 1L, LocalDate.of(2021, 1, 1));
        c4.setId(3L);
        repository.update(c4);
        Contract updatedContract = repository.findOne(3L).get();
        assertEquals("Client IDs should be equal", 3L, (long) updatedContract.getClientId());
        assertEquals("Subscription IDs be equal", 1L, (long) updatedContract.getSubscriptionId());
        assertEquals("Dates should be equal", updatedContract.getDate(), LocalDate.of(2021, 1, 1));

        repository.update(c3);
    }

    @Test(expected = ContractException.class)
    public void updateExceptionTest() throws Exception {
        Contract c4 = new Contract(3L, 1L, LocalDate.of(2021, 1, 1));
        c4.setId(5L);
        repository.update(c4);
    }
}
