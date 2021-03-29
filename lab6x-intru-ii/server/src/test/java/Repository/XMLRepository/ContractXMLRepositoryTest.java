package Repository.XMLRepository;

import intrusii.common.Domain.Contract;
import intrusii.common.Domain.Validators.ContractValidator;
import intrusii.server.Repository.Repository;
import intrusii.server.Repository.XMLRepository.ContractXMLRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ContractXMLRepositoryTest {
    private static Contract c1 = new Contract(1L,1L, LocalDate.parse("2021-02-27"));
    private static Contract c2 = new Contract(2L,2L, LocalDate.parse("2021-02-27"));
    private static Contract c3 = null;
    private Repository<Long, Contract> repo;

    @Before
    public void setUp() throws Exception {
        c1.setId(1L);
        c2.setId(2L);
        ContractValidator validator = new ContractValidator();
        repo = new ContractXMLRepository(validator, "/data/Tests/XML/ContractsXMLTest");
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
        Set<Contract> sc = new HashSet<Contract>();
        sc.add(c1);
        //Iterator<Contract> i = sc.iterator();
        assertEquals("Contracts should be equal", sc, repo.findAll());
    }

    @Test
    public void testSave() throws Exception {
        repo.save(c2);
        Contract c = repo.findOne(c2.getId()).get();
        assertEquals("Contracts should be equal", c2, c);
        repo.delete(c2.getId());
    }

    @Test(expected = NullPointerException.class)
    public void testSaveException() throws Exception {
        repo.save(c3);
    }

    @Test
    public void testDelete() throws Exception {
        repo.save(c2);
        assertEquals("Contracts should be equal", c2, repo.delete(c2.getId()).get());
    }
}
