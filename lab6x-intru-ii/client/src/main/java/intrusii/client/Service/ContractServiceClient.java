package intrusii.client.Service;

import intrusii.common.Domain.Contract;
import intrusii.common.Service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.Future;

public class ContractServiceClient implements ContractService {

    @Autowired
    private ContractService contractService;

    @Override
    public void addContract(Contract contract) {
        contractService.addContract(contract);
    }

    @Override
    public void deleteContract(Long id) {
        contractService.deleteContract(id);
    }

    @Override
    public void updateContract(Contract contract) {
        contractService.updateContract(contract);
    }

    @Override
    public List<Contract> getAllContracts() {
        return contractService.getAllContracts();
    }

    @Override
    public List<Contract> filterActiveContracts() {
        return contractService.filterActiveContracts();
    }
}
