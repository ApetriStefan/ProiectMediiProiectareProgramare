package intrusii.client.Service;

import intrusii.client.TCP.TcpClient;
import intrusii.common.Message;
import intrusii.common.SocketContractService;
import intrusii.common.SocketController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class SocketContractServiceImpl implements SocketContractService {
    private ExecutorService executorService;
    private TcpClient tcpClient;

    public SocketContractServiceImpl(ExecutorService executorService, TcpClient tcpClient){
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public Future<String> addContract(String contract) {
        return genericFunction(SocketController.ADD_CONTRACT, contract);
    }

    @Override
    public Future<String> deleteContract(String id) {
        return genericFunction(SocketController.DELETE_CONTRACT, id);
    }

    @Override
    public Future<String> updateContract(String contract) {
        return genericFunction(SocketController.UPDATE_CONTRACT, contract);
    }

    @Override
    public Future<String> getAllContracts() {
        return genericFunction(SocketController.GET_ALL_CONTRACTS, null);
    }

    @Override
    public Future<String> filterExpiredContracts() {
        return genericFunction(SocketController.FILTER_EXPIRED_CONTRACTS, null);
    }



    public Future<String> genericFunction(String command, String parameter){
        return executorService.submit( () -> {
            Message request = new Message(command, parameter);
            Message response = tcpClient.sendAndReceive(request);

            return response.getBody();
        });
    }
}
