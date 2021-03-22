package intrusii.client.UI;

import intrusii.common.SocketController;
import intrusii.common.SocketException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Console {
    private SocketController socketCtrl;

    public Console(SocketController socketCtrl) {
        this.socketCtrl = socketCtrl;
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
//                case "2":
//                    subscriptionMenu();
//                    break;
//                case "3":
//                    contractMenu();
//                    break;
                default:
                    System.err.println("Invalid command");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }
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
//                case "2":
//                    deleteClient();
//                    break;
//                case "3":
//                    updateClient();
//                    break;
//                case "4":
//                    printAllClients();
//                    break;
//                case "5":
//                    filterClientsByName();
//                    break;
//                case "6":
//                    filterClientsByCNP();
//                    break;
                default:
                    System.err.println("Invalid command");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }
            }
        }
    }

//    private void subscriptionMenu() {
//        String option;
//        while(true){
//            printSubscriptionMenu();
//            Scanner scannerOption = new Scanner(System.in);
//            System.out.print(">> ");
//            option = scannerOption.nextLine();
//            switch (option) {
//                case "0":
//                    return;
//                case "1":
//                    addSubscription();
//                    break;
//                case "2":
//                    deleteSubscription();
//                    break;
//                case "3":
//                    updateSubscription();
//                    break;
//                case "4":
//                    printAllSubscriptions();
//                    break;
//                case "5":
//                    filterSubscriptionsByDuration();
//                    break;
//                case "6":
//                    filterSubscriptionsByType();
//                    break;
//                default:
//                    System.err.println("Invalid command");
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException ignored){}
//            }
//        }
//    }
//
//    private void contractMenu() {
//        String option;
//        while(true){
//            printContractMenu();
//            Scanner scannerOption = new Scanner(System.in);
//            System.out.print(">> ");
//            option = scannerOption.nextLine();
//            switch (option) {
//                case "0":
//                    return;
//                case "1":
//                    addContract();
//                    break;
//                case "2":
//                    deleteContract();
//                    break;
//                case "3":
//                    updateContract();
//                    break;
//                case "4":
//                    printAllContracts();
//                    break;
//                case "5":
//                    filterExpiredContracts();
//                    break;
//                default:
//                    System.err.println("Invalid command");
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException ignored){}
//            }
//        }
//    }

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
        System.out.println("5. Show the active contracts\n");
    }

//`````````````````````````````````````````````````Client`````````````````````````````````````````````````//

    /**
     * Adds the client that has been read from console
     */
    private void addClient() {
        String client = readClient();
        if (client == null)
            System.out.println("Please insert valid data");
        else {
            Future<String> resultFuture = socketCtrl.addClient(client);
            try {
                String result = resultFuture.get();
                System.out.println(result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
    }

}

//    /**
//     * Deletes a client by an ID that has been read from console.
//     *
//     */
//    private void deleteClient() {
//        Long id = readClientID();
//        if (id == null || id < 0) {
//            System.out.println("Given id is not valid");
//            return;
//        }
//        try {
//            socketCtrl.deleteClient(id);
//            //System.out.println("Client successfully deleted");
//        } catch (ValidatorException | IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Update the Client that has been read from console
//     */
//    private void updateClient() {
//        System.out.println("Input the ID of the client to be updated and the new client");
//        Client client = readClientUpdate();
//        if (client == null) {
//            System.out.println("Please insert valid data");
//            return;
//        }
//        try {
//            socketCtrl.updateClient(client);
//            //System.out.println("Client successfully updated");
//        } catch (ValidatorException | IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Prints all clients in the repository
//     *
//     */
//    private void printAllClients() {
//        Set<Client> clients = socketCtrl.getAllClients();
//        System.out.println("The clients are:");
//        clients.forEach(System.out::println);
//    }
//
//    private void filterClientsByName() {
//        System.out.print("Enter name: ");
//        Scanner scannerName = new Scanner(System.in);
//        String name = scannerName.nextLine();
//        System.out.println("The clients containing '" + name + "' are: ");
//        socketCtrl.filterClientsByName(name).forEach(System.out::println);
//    }
//
//    private void filterClientsByCNP() {
//        System.out.print("Enter CNP: ");
//        Scanner scannerCnp = new Scanner(System.in);
//        String cnp = scannerCnp.nextLine();
//        System.out.println("The clients with this CNP are: ");
//        socketCtrl.filterClientsByCnp(cnp).forEach(System.out::println);
//    }

    /**
     * Reads a client from the keyboard.
     *
     * @return an {@code String} - null if the data was not valid, otherwise returns the entity.
     */
    private String readClient() {
        System.out.println("Read Client {CNP, Name, Email, Address}");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            String cnp = bufferRead.readLine();
            String name = bufferRead.readLine();
            String email = bufferRead.readLine();
            String address = bufferRead.readLine();

            return cnp + ";" + name + ";" + email + ";" + address;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

//    /**
//     * Reads a client from the keyboard for update.
//     *
//     * @return an {@code Client} - null if the data was not valid, otherwise returns the entity.
//     */
//    private Client readClientUpdate() {
//        System.out.println("Read Client {id, CNP, Name, Email, Address}");
//        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//        try {
//            Long id = Long.valueOf(bufferRead.readLine());
//            String CNP = bufferRead.readLine();
//            String name = bufferRead.readLine();
//            String email = bufferRead.readLine();
//            String address = bufferRead.readLine();
//
//            Client client = new Client(CNP, name, email, address);
//            client.setId(id);
//
//            return client;
//        } catch (IOException | NumberFormatException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * Reads a client ID from the keyboard.
//     *
//     * @return an {@code Long} - null if the data was not valid, otherwise returns the ID.
//     */
//    private Long readClientID() {
//        System.out.println("Read Client ID {id}");
//        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//        try {
//            return Long.valueOf(bufferRead.readLine());
//        }
//        catch (IOException | NumberFormatException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
//
////`````````````````````````````````````````````````Subscription`````````````````````````````````````````````````//
//    /**
//     * Adds the Subscription that has been read from console
//     */
//    private void addSubscription() {
//        Subscription subscription = readSubscription();
//        if (subscription == null) {
//            System.out.println("Please insert valid data");
//            return;
//        }
//        try {
//            socketCtrl.addSubscription(subscription);
//            //System.out.println("Subscription successfully added");
//        } catch (ValidatorException | IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Deletes a subscription by an ID that has been read from console.
//     *
//     */
//    private void deleteSubscription() {
//        Long id = readSubscriptionID();
//        if (id == null || id < 0) {
//            System.out.println("Given ID is not valid");
//            return;
//        }
//        try {
//            socketCtrl.deleteSubscription(id);
//            //System.out.println("Subscription successfully deleted");
//        } catch (ValidatorException | IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Update the Subscription that has been read from console
//     */
//    private void updateSubscription() {
//        System.out.println("Input the ID of the subscription to be updated and the new subscription");
//        Subscription subscription = readSubscriptionUpdate();
//        if (subscription == null || subscription.getId() < 0) {
//            System.out.println("Please insert valid data");
//            return;
//        }
//        try {
//            socketCtrl.updateSubscription(subscription);
//            //System.out.println("Subscription successfully updated");
//        } catch (ValidatorException | IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Prints all subscriptions in the repository
//     *
//     */
//    private void printAllSubscriptions() {
//        Set<Subscription> subscriptions = socketCtrl.getAllSubscriptions();
//        System.out.println("The subscriptions are: ");
//        subscriptions.forEach(System.out::println);
//    }
//
//    private void filterSubscriptionsByDuration() {
//        System.out.print("Enter duration: ");
//        Scanner scannerDuration = new Scanner(System.in);
//        String durationS = scannerDuration.nextLine();
//        try{
//            int duration = Integer.parseInt(durationS);
//            socketCtrl.filterSubscriptionByDuration(duration).forEach(System.out::println);
//        }catch (NumberFormatException ex){
//            System.err.println("Please introduce a number");
//        }
//    }
//
//    private void filterSubscriptionsByType() {
//        System.out.print("Enter type: ");
//        Scanner scannerType = new Scanner(System.in);
//        String type = scannerType.nextLine();
//        socketCtrl.filterSubscriptionByType(type).forEach(System.out::println);
//    }
//
//    /**
//     * Reads a subscription from the keyboard.
//     *
//     * @return an {@code Subscription} - null if the data was not valid, otherwise returns the entity.
//     */
//    private Subscription readSubscription() {
//        System.out.println("Read Subscription {Type, Price, Duration}");
//        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//        try {
//            String typeString = bufferRead.readLine();
//            SubscriptionType type = SubscriptionType.Default;
//            type = type.setSubscriptionType(typeString);
//            float price = Float.parseFloat(bufferRead.readLine());
//            int duration = Integer.parseInt(bufferRead.readLine());
//
//            Subscription subscription = new Subscription(type, price, duration);
//            subscription.setId(1L);
//
//            return subscription;
//        } catch (IOException | NumberFormatException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * Reads a subscription from the keyboard for update.
//     *
//     * @return an {@code Subscription} - null if the data was not valid, otherwise returns the entity.
//     */
//    private Subscription readSubscriptionUpdate() {
//        System.out.println("Read Subscription {id, Type, Price, Duration}");
//        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//        try {
//            Long id = Long.valueOf(bufferRead.readLine());
//            String typeString = bufferRead.readLine();
//            SubscriptionType type = SubscriptionType.Default;
//            type = type.setSubscriptionType(typeString);
//            float price = Float.parseFloat(bufferRead.readLine());
//            int duration = Integer.parseInt(bufferRead.readLine());
//
//            Subscription subscription = new Subscription(type, price, duration);
//            subscription.setId(id);
//
//            return subscription;
//        } catch (IOException | NumberFormatException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * Reads a subscription ID from the keyboard.
//     *
//     * @return an {@code Long} - null if the data was not valid, otherwise returns the ID.
//     */
//    private Long readSubscriptionID() {
//        System.out.println("Read Subscription ID {id}");
//        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//        try {
//            return Long.valueOf(bufferRead.readLine());
//        }
//        catch (IOException | NumberFormatException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
//
////`````````````````````````````````````````````````Contract`````````````````````````````````````````````````//
//    /**
//     * Adds the contract that has been read from console
//     */
//    private void addContract() {
//        Contract contract = readContract();
//        if (contract == null || contract.getId() < 0) {
//            System.out.println("Please insert valid data");
//            return;
//        }
//        try {
//            socketCtrl.addContract(contract);
//            //System.out.println("Contract successfully added");
//        } catch (ValidatorException | IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Deletes a contract by an ID that has been read from console.
//     *
//     */
//    private void deleteContract() {
//        Long id = readContractID();
//        if (id == null || id < 0) {
//            System.out.println("The given ID is not valid");
//            return;
//        }
//        try {
//            socketCtrl.deleteContract(id);
//            //System.out.println("Contract successfully deleted");
//        } catch (ValidatorException | IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Updates the contract that has been read from console
//     */
//    private void updateContract() {
//        Contract contract = readContractForUpdate();
//        if (contract == null || contract.getId() < 0) {
//            System.out.println("Please insert valid data");
//            return;
//        }
//        try{
//            socketCtrl.updateContract(contract);
//            //System.out.println("Contract successfully updated");
//        }catch (ValidatorException | IllegalArgumentException e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Prints all contracts in the repository
//     *
//     */
//    private void printAllContracts() {
//        Set<Contract> contracts = socketCtrl.getAllContracts();
//        System.out.println("The contracts are:");
//        contracts.forEach(System.out::println);
//    }
//
//    private void filterExpiredContracts(){
//        System.out.println("Active contracts:");
//        socketCtrl.filterActiveContracts().forEach(System.out::println);
//    }
//
//    /**
//     * Reads a contract from the keyboard.
//     *
//     * @return an {@code Contract} - null if the data was not valid, otherwise returns the entity.
//     */
//    private Contract readContract() {
//        System.out.println("Read Contract {Client ID, Subscription ID, Date<yyyy-mm-dd>}");
//        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//        try {
//            Long clientID = Long.parseLong(bufferRead.readLine());
//            Long subscriptionID = Long.parseLong(bufferRead.readLine());
//            LocalDate date = LocalDate.parse(bufferRead.readLine());
//
//            Contract contract = new Contract(clientID,subscriptionID, date);
//            contract.setId(1L);
//
//            return contract;
//        } catch (IOException | NumberFormatException | DateTimeParseException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * Reads a contract ID from the keyboard.
//     *
//     * @return an {@code Long} - null if the data was not valid, otherwise returns the ID.
//     */
//    private Long readContractID() {
//        System.out.println("Read Contract ID {id}");
//        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//        try {
//            return Long.valueOf(bufferRead.readLine());
//        }
//        catch (IOException | NumberFormatException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * Reads a contract ID and a client ID from the keyboard (because the client is the only field that is allowed to be modified).
//     *
//     * @return an {@code Contract} - null if the data was not valid, otherwise returns the entity.
//     */
//    private Contract readContractForUpdate() {
//        System.out.println("Update Contract {id, Client ID}");
//        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//        try {
//            Long id = Long.valueOf(bufferRead.readLine());// ...
//            Long clientID = Long.parseLong(bufferRead.readLine());
//
//            Contract contract = new Contract(clientID);
//            contract.setId(id);
//
//            return contract;
//        } catch (IOException | NumberFormatException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
}
