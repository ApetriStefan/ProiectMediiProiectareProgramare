import intrusii.common.SocketController;
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
import intrusii.server.Service.ClientService;
import intrusii.server.Service.ContractService;
import intrusii.server.Service.SubscriptionService;
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
        ClientService clientService = new ClientService(clientRepository);
        SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository);
        ContractService contractService = new ContractService(contractRepository);

        //TCP Connection
        TcpServer tcpServer = new TcpServer(executorService, SocketController.PORT);

        //ServerConnection
        SocketController socketController = new SocketServerController(executorService, clientService, contractService, subscriptionService);

        //Add handlers
        Handlers.addHandlerClient(tcpServer, socketController);

        //Start server
        tcpServer.startServer();

        System.out.println("Goodbye");
    }
}
