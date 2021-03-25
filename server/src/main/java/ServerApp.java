import intrusii.common.SocketClientService;
import intrusii.common.SocketContractService;
import intrusii.common.SocketController;
import intrusii.common.SocketSubscriptionService;
import intrusii.server.Controller.SocketServerController;
import intrusii.server.Domain.Client;
import intrusii.server.Domain.Contract;
import intrusii.server.Domain.Subscription;
import intrusii.server.Domain.Validators.ClientValidator;
import intrusii.server.Domain.Validators.ContractValidator;
import intrusii.server.Domain.Validators.SubscriptionValidator;
import intrusii.server.Domain.Validators.Validator;
import intrusii.server.Repository.DBRepository.ClientDBRepository;
import intrusii.server.Repository.DBRepository.ContractDBRepository;
import intrusii.server.Repository.DBRepository.SubscriptionDBRepository;
import intrusii.server.Repository.FileRepository.ClientFileRepository;
import intrusii.server.Repository.FileRepository.ContractFileRepository;
import intrusii.server.Repository.FileRepository.SubscriptionFileRepository;
import intrusii.server.Repository.InMemoryRepository;
import intrusii.server.Repository.Repository;
import intrusii.server.Repository.XMLRepository.ClientXMLRepository;
import intrusii.server.Repository.XMLRepository.ContractXMLRepository;
import intrusii.server.Repository.XMLRepository.SubscriptionXMLRepository;
import intrusii.server.Service.*;
import intrusii.server.TCP.TcpServer;
import intrusii.server.Utility.Handlers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        //Validators
        Validator<Client> clientValidator = new ClientValidator();
        Validator<Subscription> subscriptionValidator = new SubscriptionValidator();
        Validator<Contract> contractValidator = new ContractValidator();

        //Repository (from properties)
        Repository<Long, Client> clientRepository = null;
        Repository<Long, Subscription> subscriptionRepository = null;
        Repository<Long, Contract> contractRepository = null;
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("./data/config.properties"));
            switch (properties.getProperty("persistenceEngine")) {
                case "database" -> {
                    clientRepository = new ClientDBRepository(clientValidator, properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
                    subscriptionRepository = new SubscriptionDBRepository(subscriptionValidator, properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
                    contractRepository = new ContractDBRepository(contractValidator, properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
                }
                case "xml" -> {
                    clientRepository = new ClientXMLRepository(clientValidator, "./data/XML/clientsXML");
                    subscriptionRepository = new SubscriptionXMLRepository(subscriptionValidator, "./data/XML/subscriptionsXML");
                    contractRepository = new ContractXMLRepository(contractValidator, "./data/XML/contractsXML");
                }
                case "memory" -> {
                    clientRepository = new InMemoryRepository<>(clientValidator);
                    subscriptionRepository = new InMemoryRepository<>(subscriptionValidator);
                    contractRepository = new InMemoryRepository<>(contractValidator);
                }
                default -> {
                    clientRepository = new ClientFileRepository(clientValidator, "./data/File/clients");
                    subscriptionRepository = new SubscriptionFileRepository(subscriptionValidator, "./data/File/subscriptions");
                    contractRepository = new ContractFileRepository(contractValidator, "./data/File/contracts");
                }
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }

        //Service
        ClientService clientService = new ClientService(clientRepository,contractRepository);
        SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository,contractRepository);
        ContractService contractService = new ContractService(contractRepository,clientRepository,subscriptionRepository);

        SocketClientService socketClientService = new SocketClientServiceImpl(executorService,clientService,contractService);
        SocketSubscriptionService socketSubscriptionService = new SocketSubscriptionServiceImpl(executorService,subscriptionService,contractService);
        SocketContractService socketContractService = new SocketContractServiceImpl(executorService,clientService,subscriptionService,contractService);

        //Port and hostname (from properties)
        Properties connectionProperties = null;
        try {
            connectionProperties = new Properties();
            connectionProperties.load(new FileInputStream("./common/ConnectionConfig.properties"));

        }catch (IOException ex){
            ex.printStackTrace();
        }

        //TCP Connection
        TcpServer tcpServer = new TcpServer(executorService, Integer.parseInt(connectionProperties.getProperty("PORT")));

        //ServerConnection
//        SocketController socketController = new SocketServerController(executorService, clientService, contractService, subscriptionService);

        //Add handlers
        Handlers.addHandlerClient(tcpServer, socketClientService);
        Handlers.addHandlerSubscription(tcpServer, socketSubscriptionService);
        Handlers.addHandlerContract(tcpServer, socketContractService);

        //Start server
        tcpServer.startServer();

        System.out.println("Goodbye");
    }
}
