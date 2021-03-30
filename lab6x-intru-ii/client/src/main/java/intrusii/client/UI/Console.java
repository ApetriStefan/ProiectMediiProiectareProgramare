package intrusii.client.UI;

import intrusii.common.Domain.Client;
import intrusii.common.Domain.Contract;
import intrusii.common.Domain.Subscription;
import intrusii.common.Domain.SubscriptionType;
import intrusii.common.Domain.Validators.ValidatorException;
import intrusii.common.Service.ClientService;
import intrusii.common.Service.ContractService;
import intrusii.common.Service.ServiceException;
import intrusii.common.Service.SubscriptionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Console {
    private final ClientService clientService;
    private final SubscriptionService subscriptionService;
    private final ContractService contractService;

    public Console(ClientService clientService, SubscriptionService subscriptionService, ContractService contractService) {
        this.clientService = clientService;
        this.subscriptionService = subscriptionService;
        this.contractService = contractService;
    }

    public void runConsole() {
        System.out.println("Welcome!");
        mainMenu();
        System.out.println("Goodbye!");
    }

//`````````````````````````````````````````````````Menu`````````````````````````````````````````````````//
    private void mainMenu() {
        String option;
        while (true) {
            printMainMenu();
            Scanner scannerOption = new Scanner(System.in);
            System.out.print(">> ");
            option = scannerOption.nextLine();
            switch (option) {
                case "0":
                    return;
                case "1":
                    clientMenu();
                    break;
                case "2":
                    subscriptionMenu();
                    break;
                case "3":
                    contractMenu();
                    break;
                default:
                    System.err.println("Invalid command");
            }
        }
    }

    private void clientMenu() {
        String option;
        while (true) {
            printClientMenu();
            Scanner scannerOption = new Scanner(System.in);
            System.out.print(">> ");
            option = scannerOption.nextLine();
            switch (option) {
                case "0":
                    return;
                case "1":
                    addClient();
                    break;
                case "2":
                    deleteClient();
                    break;
                case "3":
                    updateClient();
                    break;
                case "4":
                    printAllClients();
                    break;
                case "5":
                    filterClientsByName();
                    break;
                case "6":
                    filterClientsByCnp();
                    break;
                default:
                    System.err.println("Invalid command");
            }
        }
    }

    private void subscriptionMenu() {
        String option;
        while (true) {
            printSubscriptionMenu();
            Scanner scannerOption = new Scanner(System.in);
            System.out.print(">> ");
            option = scannerOption.nextLine();
            switch (option) {
                case "0":
                    return;
                case "1":
                    addSubscription();
                    break;
                case "2":
                    deleteSubscription();
                    break;
                case "3":
                    updateSubscription();
                    break;
                case "4":
                    printAllSubscriptions();
                    break;
                case "5":
                    filterSubscriptionsByDuration();
                    break;
                case "6":
                    filterSubscriptionsByType();
                    break;
                default:
                    System.err.println("Invalid command");
            }
        }
    }

    private void contractMenu() {
        String option;
        while (true) {
            printContractMenu();
            Scanner scannerOption = new Scanner(System.in);
            System.out.print(">> ");
            option = scannerOption.nextLine();
            switch (option) {
                case "0":
                    return;
                case "1":
                    addContract();
                    break;
                case "2":
                    deleteContract();
                    break;
                case "3":
                    updateContract();
                    break;
                case "4":
                    printAllContracts();
                    break;
                case "5":
                    filterExpiredContracts();
                    break;
                default:
                    System.err.println("Invalid command");
            }
        }
    }

    //`````````````````````````````````````````````````Print Menu`````````````````````````````````````````````````//
    private void printMainMenu() {
        System.out.println("\n----------Menu----------");
        System.out.println("0. Exit");
        System.out.println("1. Clients menu");
        System.out.println("2. Subscriptions menu");
        System.out.println("3. Contracts menu\n");
    }

    private void printClientMenu() {
        System.out.println("\n----------Client Menu----------");
        System.out.println("0. Back to main menu");
        System.out.println("1. Add a client");
        System.out.println("2. Delete a client");
        System.out.println("3. Update a client");
        System.out.println("4. Show all clients");
        System.out.println("5. Filter clients by name");
        System.out.println("6. Filter clients by CNP\n");
    }

    private void printSubscriptionMenu() {
        System.out.println("\n----------Subscription Menu----------");
        System.out.println("0. Back to main menu");
        System.out.println("1. Add a subscription");
        System.out.println("2. Delete a subscription");
        System.out.println("3. Update a subscription");
        System.out.println("4. Show all subscriptions");
        System.out.println("5. Filter subscriptions by duration");
        System.out.println("6. Filter subscriptions by type\n");
    }

    private void printContractMenu() {
        System.out.println("\n----------Contract Menu----------");
        System.out.println("0. Back to main menu");
        System.out.println("1. Add a contract");
        System.out.println("2. Delete a contract");
        System.out.println("3. Update a contract");
        System.out.println("4. Show all contracts");
        System.out.println("5. Show the active contracts");
    }

//`````````````````````````````````````````````````Client`````````````````````````````````````````````````//

    /**
     * Adds the client that has been read from console
     */
    private void addClient() {
        Client client = readClient();
        if (client != null)
            try{
                clientService.addClient(client);
                System.out.println("Client successfully added");
            }catch (ValidatorException ex){
                System.err.println(ex.getMessage());
            }
    }

    /**
     * Deletes a client by an ID that has been read from console.
     */
    private void deleteClient() {
        System.out.print("Read Client ID: ");
        Long id = readLong();
        if (id != null)
            try{
                clientService.deleteClient(id);
                System.out.println("Client successfully deleted");
            }catch (ValidatorException | ServiceException ex){
                System.err.println(ex.getMessage());
            }
    }

    /**
     * Update the Client that has been read from console
     */
    private void updateClient() {
        Client client = readClientUpdate();
        if (client != null)
            try{
                clientService.updateClient(client);
                System.out.println("Client successfully updated");
            }catch (ValidatorException | ServiceException ex){
                System.err.println(ex.getMessage());
            }
    }

    /**
     * Prints all clients in the repository
     *
     */
    private void printAllClients() {
        System.out.println("The clients are:");
        clientService.getAllClients().forEach(System.out::println);
    }

    private void filterClientsByName() {
        System.out.print("Enter name: ");
        String name = readString();

        if (name != null){
            System.out.println("The clients containing '" + name + "' are:");
            clientService.filterClientsByName(name).forEach(System.out::println);
        }
    }

    private void filterClientsByCnp() {
        System.out.print("Enter CNP: ");
       String cnp = readString();

       if(cnp != null) {
           System.out.println("The client with this cnp is:");
           clientService.filterClientsByCnp(cnp).forEach(System.out::println);
       }
    }

    /**
     * Reads a client from the keyboard.
     *
     * @return an {@code Client} - null if the data was not valid, otherwise returns the entity.
     */
    private Client readClient() {
        System.out.println("Read Client {CNP, Name, Email, Address}: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            String cnp = bufferRead.readLine();
            String name = bufferRead.readLine();
            String email = bufferRead.readLine();
            String address = bufferRead.readLine();

            return new Client(cnp, name, email, address);
        } catch (IOException ex) {
            System.err.println("Something went wrong! Check the file");
            return null;
        }
    }

    /**
     * Reads a client from the keyboard for update.
     *
     * @return an {@code Client} - null if the data was not valid, otherwise returns the entity.
     */
    private Client readClientUpdate() {
        System.out.println("Read Client {ID, CNP, Name, Email, Address}: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            Long id = Long.valueOf(bufferRead.readLine());
            String cnp = bufferRead.readLine();
            String name = bufferRead.readLine();
            String email = bufferRead.readLine();
            String address = bufferRead.readLine();

            Client client = new Client(cnp, name, email, address);
            client.setId(id);
            return client;
        } catch (IOException ex) {
            System.err.println("Something went wrong! Check the file");
            return null;
        }
        catch (NumberFormatException ex){
            System.err.println("Invalid id! The id should be of type integer.");
            return null;
        }
    }

//`````````````````````````````````````````````````Subscription`````````````````````````````````````````````````//
    /**
     * Adds the Subscription that has been read from console
     */
    private void addSubscription() {
        Subscription subscription = readSubscription();
        if (subscription != null)
            try{
                subscriptionService.addSubscription(subscription);
                System.out.println("Subscription successfully added");
            }catch (ValidatorException ex){
                System.err.println(ex.getMessage());
            }
    }

    /**
     * Deletes a subscription by an ID that has been read from console.
     *
     */
    private void deleteSubscription() {
        System.out.print("Read Subscription ID: ");
        Long id = readLong();
        if (id != null)
            try{
                subscriptionService.deleteSubscription(id);
                System.out.println("Subscription successfully deleted");
            }catch (ValidatorException | ServiceException ex){
                System.err.println(ex.getMessage());
            }
    }

    /**
     * Update the Subscription that has been read from console
     */
    private void updateSubscription() {
        Subscription subscription = readSubscriptionUpdate();
        if (subscription != null)
            try{
                subscriptionService.updateSubscription(subscription);
                System.out.println("Subscription successfully updated");
            }catch (ValidatorException | ServiceException ex){
                System.err.println(ex.getMessage());
            }
    }

    /**
     * Prints all subscriptions in the repository
     *
     */
    private void printAllSubscriptions() {
        System.out.println("The subscriptions are:");
        subscriptionService.getAllSubscriptions().forEach(System.out::println);
    }

    private void filterSubscriptionsByDuration() {
        System.out.print("Enter duration: ");
        Integer duration = readInteger();

        if(duration != null) {
            System.out.println("The subscription with duration '" + duration + "' are:");
            subscriptionService.filterSubscriptionByDuration(duration).forEach(System.out::println);
        }
    }

    private void filterSubscriptionsByType() {
        System.out.print("Enter type: ");
        SubscriptionType type = readSubscriptionType();

        if (type != null) {
            System.out.println("The subscription of type '" + type.getLabel() + "' are:");
            subscriptionService.filterSubscriptionByType(type).forEach(System.out::println);
        }
    }

    /**
     * Reads a subscription from the keyboard.
     *
     * @return an {@code Subscription} - null if the data was not valid, otherwise returns the entity.
     */
    private Subscription readSubscription() {
        System.out.println("Read Subscription {Type, Price, Duration}: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            SubscriptionType type = SubscriptionType.valueOf(bufferRead.readLine());
            float price = Float.parseFloat(bufferRead.readLine());
            int duration = Integer.parseInt(bufferRead.readLine());

            return new Subscription(type, price, duration);
        } catch (IOException ex) {
            System.err.println("Something went wrong! Check the file");
            return null;
        }
        catch (IllegalArgumentException ex){
            System.out.println("There is no type with this name!");
            return null;
        }
    }

    /**
     * Reads a subscription from the keyboard for update.
     *
     * @return an {@code Subscription} - null if the data was not valid, otherwise returns the entity.
     */
    private Subscription readSubscriptionUpdate() {
        System.out.println("Read Subscription {id, Type, Price, Duration}");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            Long id = Long.valueOf(bufferRead.readLine());
            SubscriptionType type = SubscriptionType.valueOf(bufferRead.readLine());
            float price = Float.parseFloat(bufferRead.readLine());
            int duration = Integer.parseInt(bufferRead.readLine());

            Subscription subscription = new Subscription(type, price, duration);
            subscription.setId(id);
            return subscription;
        } catch (IOException ex) {
            System.err.println("Something went wrong! Check the file");
            return null;
        }
        catch (NumberFormatException ex){
            System.err.println("Invalid input! The required type is integer.");
            return null;
        }
        catch (IllegalArgumentException ex){
            System.err.println("There is no type with this name!");
            return null;
        }
    }

//`````````````````````````````````````````````````Contract`````````````````````````````````````````````````//
    /**
     * Adds the contract that has been read from console
     */
    private void addContract() {
        Contract contract = readContract();
        if (contract != null)
            try {
                contractService.addContract(contract);
                System.out.println("Contract successfully added");
            }catch (ValidatorException | ServiceException ex){
                System.err.println(ex.getMessage());
            }
    }

    /**
     * Deletes a contract by an ID that has been read from console.
     *
     */
    private void deleteContract() {
        System.out.print("Read Contract ID: ");
        Long id = readLong();
        if(id != null)
            try {
                contractService.deleteContract(id);
                System.out.println("Contract successfully deleted");
            }catch (ValidatorException ex){
                System.err.println(ex.getMessage());
            }
    }

    /**
     * Updates the contract that has been read from console
     */
    private void updateContract() {
        Contract contract = readContractForUpdate();
        if (contract != null)
            try {
                contractService.updateContract(contract);
                System.out.println("Contract successfully updated");
            }catch (ValidatorException | ServiceException ex){
                System.err.println(ex.getMessage());
            }
    }

    /**
     * Prints all contracts in the repository
     *
     */
    private void printAllContracts() {
        System.out.println("The contracts are:");
        contractService.getAllContracts().forEach(contract->{
            System.out.println("Id{"+ contract.getId()+"}");
            System.out.println(clientService.getClientByID(contract.getClientId()));
            System.out.println(subscriptionService.getSubscriptionByID(contract.getSubscriptionId()));
            System.out.println("Date{"+contract.getDate()+"}"+"\n");
        });
    }

    private void filterExpiredContracts() {
        System.out.println("The active contracts are:");
        contractService.filterActiveContracts().forEach(System.out::println);
    }

    /**
     * Reads a contract from the keyboard.
     *
     * @return an {@code Contract} - null if the data was not valid, otherwise returns the entity.
     */
    private Contract readContract() {
        System.out.println("Read Contract {Client ID, Subscription ID, Date<yyyy-mm-dd>}: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            Long clientID = Long.parseLong(bufferRead.readLine());
            Long subscriptionID = Long.parseLong(bufferRead.readLine());
            LocalDate date = LocalDate.parse(bufferRead.readLine());

            return new Contract(clientID,subscriptionID, date);
        } catch (IOException ex) {
            System.err.println("Something went wrong! Check the file");
            return null;
        }
        catch (NumberFormatException ex){
            System.err.println("Invalid input! The required type is long.");
            return null;
        }
        catch (DateTimeParseException ex){
            System.err.println("Invalid format for the date!");
            return null;
        }
    }

    /**
     * Reads a contract ID and a client ID from the keyboard (because the client is the only field that is allowed to be modified).
     *
     * @return an {@code Contract} - null if the data was not valid, otherwise returns the entity.
     */
    private Contract readContractForUpdate() {
        System.out.println("Update Contract {id, Client ID}");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            Long id = Long.parseLong(bufferRead.readLine());
            Long clientID = Long.parseLong(bufferRead.readLine());
            Long subscriptionID = Long.parseLong(bufferRead.readLine());
            LocalDate date = LocalDate.parse(bufferRead.readLine());

            Contract contract = new Contract(clientID,subscriptionID, date);
            contract.setId(id);
            return contract;
        } catch (IOException ex) {
            System.err.println("Something went wrong! Check the file");
            return null;
        }
        catch (NumberFormatException ex){
            System.err.println("Invalid input! The required type is long.");
            return null;
        }
        catch (DateTimeParseException ex){
            System.err.println("Invalid format for the date!");
            return null;
        }
    }

//`````````````````````````````````````````````````Common`````````````````````````````````````````````````//
    private Integer readInteger(){
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            return Integer.parseInt(bufferRead.readLine());
        }
        catch (IOException ex) {
            System.err.println("Something went wrong! Check the file");
            return null;
        }
        catch (NumberFormatException ex){
            System.err.println("Invalid input! The required type is integer.");
            return null;
        }
    }

    private Long readLong(){
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            return Long.valueOf(bufferRead.readLine());
        }
        catch (IOException ex) {
            System.err.println("Something went wrong! Check the file");
            return null;
        }
        catch (NumberFormatException ex){
            System.err.println("Invalid input! The required type is long.");
            return null;
        }
    }

    private String readString(){
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            return bufferRead.readLine();
        }
        catch (IOException ex) {
            System.err.println("Something went wrong! Check the file");
            return null;
        }
    }

    private SubscriptionType readSubscriptionType(){
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            return SubscriptionType.valueOf(bufferRead.readLine());
        }
        catch (IOException ex) {
            System.err.println("Something went wrong! Check the file");
            return null;
        }
        catch (IllegalArgumentException ex){
            System.err.println("There is no type with this name!");
            return null;
        }
    }
}
