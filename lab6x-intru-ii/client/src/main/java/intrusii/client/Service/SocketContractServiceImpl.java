package intrusii.client.Service;

import intrusii.client.TCP.TcpClient;
import intrusii.common.Message;
import intrusii.common.SocketContractService;
import intrusii.common.SocketContractService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class SocketContractServiceImpl implements SocketContractService {
    private final ExecutorService executorService;
    private final TcpClient tcpClient;

    public SocketContractServiceImpl(ExecutorService executorService, TcpClient tcpClient){
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public Future<String> addContract(String contract) {
        return genericFunction(SocketContractService.ADD_CONTRACT, contract);
    }

    @Override
    public Future<String> deleteContract(String id) {
        return genericFunction(SocketContractService.DELETE_CONTRACT, id);
    }

    @Override
    public Future<String> updateContract(String contract) {
        return genericFunction(SocketContractService.UPDATE_CONTRACT, contract);
    }

    @Override
    public Future<String> getAllContracts() {
        return genericFunction(SocketContractService.GET_ALL_CONTRACTS, null);
    }

    @Override
    public Future<String> filterActiveContracts() {
        return genericFunction(SocketContractService.FILTER_EXPIRED_CONTRACTS, null);
    }

    public Future<String> genericFunction(String command, String parameter){
        return executorService.submit( () -> {
            Message request = new Message(command, parameter);
            Message response = tcpClient.sendAndReceive(request);

            return response.getBody();
        });
    }
}
