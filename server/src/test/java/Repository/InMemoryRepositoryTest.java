package Repository;

import intrusii.server.Domain.BaseEntity;
import intrusii.server.Domain.Validators.BaseEntityValidator;
import intrusii.server.Repository.InMemoryRepository;
import intrusii.server.Repository.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class InMemoryRepositoryTest {
    private static final BaseEntity<Long> b1 = new BaseEntity<>();
    private static final BaseEntity<Long> b2 = new BaseEntity<>();
    private static final BaseEntity<Long> b3 = null;
    private Repository<Long, BaseEntity<Long>> repo;
    
    @Before
    public void setUp() throws Exception {
        b1.setId(1L);
        b2.setId(2L);
        BaseEntityValidator validator = new BaseEntityValidator();
        repo = new InMemoryRepository<>(validator);
        repo.save(b1);
    }

    @After
    public void tearDown() throws Exception {
        repo = null;
    }

    @Test
    public void testFindOne() throws Exception {
        assertEquals("Entities should be equal", b1, repo.findOne(1L).get());
    }

    @Test
    public void testFindAll() throws Exception {
        Set<BaseEntity<Long>> sb = new HashSet<>();
        sb.add(b1);
        assertEquals("Entities should be equal", sb, repo.findAll());
    }

    @Test
    public void testSave() throws Exception {
        repo.save(b2);
        BaseEntity<Long> b = repo.findOne(2L).get();
        assertEquals("Entities should be equal", b2, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveException() throws Exception {
        repo.save(b3);
    }

    @Test
    public void testDelete() throws Exception {
        assertEquals("Entities should be equal", b1, repo.delete(b1.getId()).get());
    }

    @Test
    public void testUpdate() throws Exception {
        assertEquals("Entities should be equal", b1, repo.update(b1).get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateException() throws Exception {
        repo.update(b3);
    }
}