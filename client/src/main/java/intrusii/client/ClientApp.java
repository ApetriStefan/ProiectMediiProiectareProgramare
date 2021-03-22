package intrusii.client;


import intrusii.client.Controller.SocketClientController;
import intrusii.client.TCP.TcpClient;
import intrusii.client.UI.Console;
import intrusii.common.SocketController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientApp {
        public static void main(String[] args) {
                ExecutorService executorService = Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors()
                );

                //TCP Connection
                TcpClient tcpClient = new TcpClient(executorService);

                //Client Controller
                SocketController socketController = new SocketClientController(executorService, tcpClient);

                //Console
                Console console = new Console(socketController);
                console.runConsole();
        }
}

