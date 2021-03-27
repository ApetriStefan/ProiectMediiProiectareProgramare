package intrusii.server.Service;
import intrusii.common.*;
import intrusii.server.Domain.Contract;
import intrusii.server.Domain.Subscription;
import intrusii.server.Domain.Validators.ValidatorException;
import intrusii.server.Utility.ContractUtil;
import intrusii.server.Utility.SubscriptionUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


public class SocketContractServiceImpl implements SocketContractService{

    private final ExecutorService executorService;
    private final ContractService contractService;
    private final ClientService clientService;
    private final SubscriptionService subscriptionService;

    public SocketContractServiceImpl(ExecutorService executorService,ClientService clientService,SubscriptionService subscriptionService,ContractService contractService) {
        this.executorService = executorService;
        this.contractService = contractService;
        this.clientService = clientService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public Future<String> addContract(String contract) {

        return executorService.submit( () -> {
            try{
                Contract contractObj = ContractUtil.StringToContract(contract);
                contractService.addContract(contractObj);
                return "Contract successfully added";
            }catch (SocketException | ValidatorException e) {
                return e.getMessage();
            }
        });
    }

    @Override
    public Future<String> deleteContract(String id) {

        return executorService.submit( () -> {
            try{
                Long idLong = Long.parseLong(id);
                contractService.deleteContract(idLong);
                return "Contract successfully deleted";
            }catch (SocketException |ValidatorException e){
                return e.getMessage();
            }
            catch (NumberFormatException e){
                return "The id should be an integer";
            }
        });
    }

    @Override
    public Future<String> updateContract(String contract) {

        return executorService.submit( () -> {
            try{
                Contract contractObj = ContractUtil.StringToContractWithId(contract);
                contractService.updateContract(contractObj);
                return "Contract successfully updated";
            }catch (SocketException | ValidatorException e){
                return e.getMessage();
            }
        });
    }

    @Override
    public Future<String> getAllContracts() {

        return executorService.submit( () -> {
            Set<Contract> contracts = contractService.getAllContracts();
            return "The contracts are:;" + ContractUtil.SetToString(contracts, clientService, subscriptionService);
        });
    }

    @Override
    public Future<String> filterActiveContracts() {

        return executorService.submit( () -> {
            List<Contract> contractList = contractService.filterActiveContracts();
            Set<Contract> contractSet = new HashSet<>(contractList);
            return "The active contracts are:;" + ContractUtil.SetToString(contractSet, clientService, subscriptionService);
        });
    }
}
