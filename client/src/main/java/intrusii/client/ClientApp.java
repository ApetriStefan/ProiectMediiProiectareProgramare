package intrusii.client;


import intrusii.client.Service.SocketClientServiceImpl;
import intrusii.client.Service.SocketContractServiceImpl;
import intrusii.client.Service.SocketSubscriptionServiceImpl;
import intrusii.client.TCP.TcpClient;
import intrusii.client.UI.Console;
import intrusii.common.SocketClientService;
import intrusii.common.SocketContractService;
import intrusii.common.SocketSubscriptionService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientApp {
        public static void main(String[] args) {

                ExecutorService executorService = Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors()
                );
                Properties connectionProperties = null;
                try {
                        connectionProperties = new Properties();
                        connectionProperties.load(new FileInputStream("./common/ConnectionConfig.properties"));

                }catch (IOException ex){
                        ex.printStackTrace();
                }

                //TCP Connection
                TcpClient tcpClient = new TcpClient(executorService,connectionProperties.getProperty("HOST"),Integer.parseInt(connectionProperties.getProperty("PORT")));

                //Client Controller
                SocketClientService socketClientService = new SocketClientServiceImpl(executorService,tcpClient);
                SocketSubscriptionService socketSubscriptionService = new SocketSubscriptionServiceImpl(executorService,tcpClient);
                SocketContractService socketContractService = new SocketContractServiceImpl(executorService,tcpClient);

                //Console
                Console console = new Console(socketClientService,socketSubscriptionService,socketContractService);
                console.runConsole();
        }
}

