package intrusii.server.Config;

import intrusii.common.Domain.Client;
import intrusii.common.Domain.Contract;
import intrusii.common.Domain.Subscription;
import intrusii.server.Repository.FileRepository.ClientFileRepository;
import intrusii.server.Repository.FileRepository.ContractFileRepository;
import intrusii.server.Repository.FileRepository.SubscriptionFileRepository;
import intrusii.server.Service.ClientServiceServer;
import intrusii.server.Service.ContractServiceServer;
import intrusii.server.Service.SubscriptionServiceServer;
import intrusii.common.Domain.Validators.ClientValidator;
import intrusii.common.Domain.Validators.ContractValidator;
import intrusii.common.Domain.Validators.SubscriptionValidator;
import intrusii.common.Domain.Validators.Validator;
import intrusii.common.Service.ClientService;
import intrusii.common.Service.ContractService;
import intrusii.common.Service.SubscriptionService;
import intrusii.server.Repository.Repository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;

@Configuration
public class ServerConfig {

//`````````````````````````````````````````````````Exporter Bean`````````````````````````````````````````````````//
    @Bean
    RmiServiceExporter rmiServiceExporterClient() {
        RmiServiceExporter rmiServiceExporterClient = new RmiServiceExporter();
        rmiServiceExporterClient.setServiceInterface(ClientService.class);
        rmiServiceExporterClient.setService(new ClientServiceServer(clientRepository(), contractRepository()));
        rmiServiceExporterClient.setServiceName("ClientService");
        return rmiServiceExporterClient;
    }

    @Bean
    RmiServiceExporter rmiServiceExporterSubscription() {
        RmiServiceExporter rmiServiceExporterSubscription = new RmiServiceExporter();
        rmiServiceExporterSubscription.setServiceInterface(SubscriptionService.class);
        rmiServiceExporterSubscription.setService(new SubscriptionServiceServer(subscriptionRepository(), contractRepository()));
        rmiServiceExporterSubscription.setServiceName("SubscriptionService");
        return rmiServiceExporterSubscription;
    }

    @Bean
    RmiServiceExporter rmiServiceExporterContract() {
        RmiServiceExporter rmiServiceExporterContract = new RmiServiceExporter();
        rmiServiceExporterContract.setServiceInterface(ContractService.class);
        rmiServiceExporterContract.setService(new ContractServiceServer(contractRepository(), clientRepository(), subscriptionRepository()));
        rmiServiceExporterContract.setServiceName("ContractService");
        return rmiServiceExporterContract;
    }

//`````````````````````````````````````````````````Validators`````````````````````````````````````````````````//
    @Bean
    Validator<Client> clientValidator(){
        return new ClientValidator();
    }

    @Bean
    Validator<Subscription> subscriptionValidator(){
        return new SubscriptionValidator();
    }

    @Bean
    Validator<Contract>contractValidator(){
        return new ContractValidator();
    }

//`````````````````````````````````````````````````Repository`````````````````````````````````````````````````//
    @Bean
    Repository<Long, Client> clientRepository(){
        return new ClientFileRepository(clientValidator(), "C:\\Users\\savin\\Desktop\\university\\second year\\second semester\\MPP\\lab5x-intru-ii\\lab6x-intru-ii\\data\\File\\Clients");
    }

    @Bean
    Repository<Long, Subscription> subscriptionRepository(){
        return new SubscriptionFileRepository(subscriptionValidator(), "C:\\Users\\savin\\Desktop\\university\\second year\\second semester\\MPP\\lab5x-intru-ii\\lab6x-intru-ii\\data\\File\\Subscriptions");
    }

    @Bean
    Repository<Long, Contract> contractRepository(){
        return new ContractFileRepository(contractValidator(), "C:\\Users\\savin\\Desktop\\university\\second year\\second semester\\MPP\\lab5x-intru-ii\\lab6x-intru-ii\\data\\File\\Contracts");
    }

//    @Bean
//    Repository<Long, Client> clientRepository(){
//        return new ClientDBRepository(clientValidator());
//    }
//
//    @Bean
//    Repository<Long, Subscription> subscriptionRepository(){
//        return new SubscriptionDBRepository(subscriptionValidator());
//    }
//
//    @Bean
//    Repository<Long, Contract> contractRepository(){
//        return new ContractDBRepository(contractValidator());
//    }
}
