package intrusii.common.Service;

import intrusii.common.Domain.Contract;

import java.util.List;
import java.util.concurrent.Future;

public interface ContractService {

    void addContract(Contract contract);

    void deleteContract(Long id);

    void updateContract(Contract contract);

    List<Contract> getAllContracts();

    List<Contract> filterActiveContracts();
}
