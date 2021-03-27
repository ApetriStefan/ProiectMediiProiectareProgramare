//package Service;
//
//import intrusii.server.Domain.Contract;
//import intrusii.server.Domain.Validators.ContractValidator;
//import intrusii.server.Repository.FileRepository.ContractFileRepository;
//import intrusii.server.Repository.Repository;
//import intrusii.server.Service.ContractService;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.time.LocalDate;
//import java.util.HashSet;
//import java.util.Set;
//
//import static org.junit.Assert.assertEquals;
//
//public class ContractServiceTest {
//    private ContractValidator contractValidator;
//    private Repository<Long, Contract> contractRepository;
//    private ContractService contractService;
//    private static final Contract c1 = new Contract(2L,3L, LocalDate.parse("2021-03-05"));
//    private static final Contract c2 = new Contract(1L,2L, LocalDate.parse("2020-02-02"));
//    private static final Contract c3 = new Contract(3L,2L, LocalDate.parse("2020-05-07"));
//
//    @Before
//    public void setUp() throws Exception {
//        c1.setId(1L);
//        c2.setId(2L);
//        contractValidator = new ContractValidator();
//        contractRepository = new ContractFileRepository(contractValidator, "/data/Tests/ServiceFile/ContractsServiceTest");
//        contractService = new ContractService(contractRepository);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        contractService = null;
//        contractRepository = null;
//        contractValidator = null;
//    }
//
//    @Test
//    public void testAddContract() throws Exception {
//        contractService.addContract(c3);
//        assertEquals("Contracts should be equal", c3, contractRepository.findOne(c3.getId()).get());
//        contractService.deleteContract(c3.getId());
//    }
//
//    @Test
//    public void testDeleteContract() throws Exception {
//        contractService.addContract(c3);
//        contractService.deleteContract(c3.getId());
//        Set<Contract> contractSet = new HashSet<>();
//        contractSet.add(c1);
//        contractSet.add(c2);
//        assertEquals("Contracts should be equal", contractSet, contractService.getAllContracts());
//    }
//
//    @Test
//    public void testUpdateContract() throws Exception {
//        c3.setId(c2.getId());
//        contractService.updateContract(c3);
//        assert contractService.getContractById(c3.getId()).getClientId() == c3.getClientId() : "The new contract should be present";
//        contractService.updateContract(c2);
//    }
//
//    @Test
//    public void testDeleteContractsByClientID() throws Exception {
//        Contract c4 = new Contract(6L,2L, LocalDate.parse("2022-05-07"));
//        contractService.addContract(c4);
//        contractService.deleteContractsByClientID(6L);
//        Set<Contract> contractSet = new HashSet<>();
//        contractSet.add(c1);
//        contractSet.add(c2);
//        assertEquals("Contracts should be equal", contractSet, contractService.getAllContracts());
//    }
//
//    @Test
//    public void testDeleteContractsBySubscriptionID() throws Exception {
//        Contract c4 = new Contract(5L,6L, LocalDate.parse("2022-05-07"));
//        contractService.addContract(c4);
//        contractService.deleteContractsBySubscriptionID(6L);
//        Set<Contract> contractSet = new HashSet<>();
//        contractSet.add(c1);
//        contractSet.add(c2);
//        assertEquals("Contracts should be equal", contractSet, contractService.getAllContracts());
//    }
//
//    @Test
//    public void testGetContractById() throws Exception {
//        assert contractService.getContractById(1L).equals(c1) : "Contracts should be equal";
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testGetContractByIdException() throws Exception {
//        contractService.getContractById(5L);
//    }
//
//    @Test
//    public void testGetAllContracts() throws Exception {
//        Set<Contract> contractSet = new HashSet<>();
//        contractSet.add(c1);
//        contractSet.add(c2);
//        assertEquals("Contracts should be equal", contractSet, contractService.getAllContracts());
//    }
//
//    @Test
//    public void testFilteredByClientID() throws Exception {
//        assert contractService.filteredByClientID(2L).get(0).equals(c1) : "Contract should be present in list";
//    }
//
//    @Test
//    public void testFilteredBySubscriptionID() throws Exception {
//        assert contractService.filteredBySubscriptionID(2L).get(0).equals(c2) : "Contract should be present in list";
//    }
//
//    @Test
//    public void testVerifyActiveContract() throws Exception {
//        Contract c4 = new Contract(1L, 2L, LocalDate.parse("2021-12-12"));
//        Contract c5 = new Contract(1L, 2L, LocalDate.parse("2015-12-12"));
//        assert contractService.verifyActiveContract(c4, 12) : "The contract should be active";
//        assert !contractService.verifyActiveContract(c5, 12) : "The contract should be expired";
//    }
//}
