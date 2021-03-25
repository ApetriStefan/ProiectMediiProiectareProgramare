package intrusii.common;

import java.util.concurrent.Future;

public interface SocketContractService {

    //`````````````````````````````````````````````````Contract`````````````````````````````````````````````````//

    String ADD_CONTRACT = "addContract";
    String DELETE_CONTRACT = "deleteContract";
    String UPDATE_CONTRACT = "updateContract";
    String GET_ALL_CONTRACTS = "getAllContracts";
    String FILTER_EXPIRED_CONTRACTS= "filterExpiredContracts";

    Future<String> addContract(String contract);
    Future<String> deleteContract(String id);
    Future<String> updateContract(String contract);
    Future<String> getAllContracts();
    Future<String> filterExpiredContracts();

}
