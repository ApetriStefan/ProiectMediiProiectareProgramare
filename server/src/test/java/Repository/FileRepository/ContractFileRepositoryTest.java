package Repository.FileRepository;

import intrusii.server.Domain.Contract;
import intrusii.server.Domain.Validators.ContractException;
import intrusii.server.Domain.Validators.ContractValidator;
import intrusii.server.Repository.FileRepository.ContractFileRepository;
import intrusii.server.Repository.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ContractFileRepositoryTest {
    private static final Contract c1 = new Contract(1L,1L, LocalDate.parse("2000-01-01"));
    private static final Contract c2 = new Contract(2L,2L, LocalDate.parse("2000-02-02"));
    private static final Contract c3 = null;
    private Repository<Long, Contract> repo;

    @Before
    public void setUp() throws Exception {
        c1.setId(1L);
        c2.setId(5L);
        ContractValidator validator = new ContractValidator();
        repo = new ContractFileRepository(validator, "/data/Tests/File/ContractsFileTest");
    }

    @After
    public void tearDown() throws Exception {
        repo = null;
    }

    @Test
    public void testFindOne() throws Exception {
        assertEquals("Contracts should be equal", c1, repo.findOne(1L).get());
    }

    @Test
    public void testFindAll() throws Exception {
        Set<Contract> sc = new HashSet<>();
        sc.add(c1);
        assertEquals("Contracts should be equal", sc, repo.findAll());
    }

    @Test
    public void testSave() throws Exception {
        repo.save(c2);
        Contract c = repo.findOne(5L).get();
        assertEquals("Contracts should be equal", c2, c);
    }

    @Test(expected = NullPointerException.class)
    public void testSaveException() throws Exception {
        repo.save(c3);
    }

    @Test
    public void testUpdate() throws Exception {
        c1.setDate(LocalDate.parse("1111-11-11"));
        repo.update(c1);
        Contract c = repo.findOne(1L).get();
        assertEquals("Contracts should be equal", c1, c);
        c1.setDate(LocalDate.parse("2000-01-01"));
        repo.update(c1);
    }

    @Test
    public void testDelete() throws Exception {
        assertEquals("Contracts should be equal", c2, repo.delete(c2.getId()).get());
    }

    @Test(expected = ContractException.class)
    public void testUpdateException() throws Exception {
        repo.update(c3);
    }
}
