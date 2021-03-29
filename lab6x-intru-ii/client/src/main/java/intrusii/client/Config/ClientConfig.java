package intrusii.client.Config;

import intrusii.client.Service.ClientServiceClient;
import intrusii.client.Service.ContractServiceClient;
import intrusii.client.Service.SubscriptionServiceClient;
import intrusii.client.UI.Console;
import intrusii.common.Service.ClientService;
import intrusii.common.Service.ContractService;
import intrusii.common.Service.SubscriptionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;


@Configuration
public class ClientConfig {
//`````````````````````````````````````````````````Proxy Bean`````````````````````````````````````````````````//
    @Bean
    RmiProxyFactoryBean rmiProxyFactoryBeanClient() {
        RmiProxyFactoryBean rmiProxyFactoryBeanClient = new RmiProxyFactoryBean();
        rmiProxyFactoryBeanClient.setServiceUrl("rmi://localhost:1099/ClientService");
        rmiProxyFactoryBeanClient.setServiceInterface(ClientService.class);
        return rmiProxyFactoryBeanClient;
    }

    @Bean
    RmiProxyFactoryBean rmiProxyFactoryBeanSubscription() {
        RmiProxyFactoryBean rmiProxyFactoryBeanSubscription = new RmiProxyFactoryBean();
        rmiProxyFactoryBeanSubscription.setServiceUrl("rmi://localhost:1099/SubscriptionService");
        rmiProxyFactoryBeanSubscription.setServiceInterface(SubscriptionService.class);
        return rmiProxyFactoryBeanSubscription;
    }

    @Bean
    RmiProxyFactoryBean rmiProxyFactoryBeanContract() {
        RmiProxyFactoryBean rmiProxyFactoryBeanContract = new RmiProxyFactoryBean();
        rmiProxyFactoryBeanContract.setServiceUrl("rmi://localhost:1099/ContractService");
        rmiProxyFactoryBeanContract.setServiceInterface(ContractService.class);
        return rmiProxyFactoryBeanContract;
    }

//`````````````````````````````````````````````````Service`````````````````````````````````````````````````//
    @Bean
    ClientService clientServiceClient() {
        return new ClientServiceClient();
    }

    @Bean
    SubscriptionService subscriptionServiceClient() {
        return new SubscriptionServiceClient();
    }

    @Bean
    ContractService contractServiceClient() {
        return new ContractServiceClient();
    }

//`````````````````````````````````````````````````Console`````````````````````````````````````````````````//
    @Bean
    Console console() {
        return new Console(clientServiceClient(), subscriptionServiceClient(), contractServiceClient());
    }
}
