package intrusii.client.UI;

import intrusii.common.SocketClientService;
import intrusii.common.SocketContractService;
import intrusii.common.SocketSubscriptionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Console {
    private final SocketClientService socketClientService;
    private final SocketSubscriptionService socketSubscriptionService;
    private final SocketContractService socketContractService;

    public Console(SocketClientService socketClientService, SocketSubscriptionService socketSubscriptionService, SocketContractService socketContractService) {
        this.socketClientService = socketClientService;
        this.socketSubscriptionService = socketSubscriptionService;
        this.socketContractService = socketContractService;
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
        String client = readClient();
        if (client == null)
            System.err.println("Please insert valid data");
        else
            CompletableFuture.supplyAsync(() -> socketClientService.addClient(client)).thenAccept(this::printResponse);
    }

    /**
     * Deletes a client by an ID that has been read from console.
     */
    private void deleteClient() {
        String id = readClientID();
        if (id == null)
            System.err.println("Invalid ID");
        else
            CompletableFuture.supplyAsync(() -> socketClientService.deleteClient(id)).thenAccept(this::printResponse);
    }

    /**
     * Update the Client that has been read from console
     */
    private void updateClient() {
        String client = readClientUpdate();
        if (client == null)
            System.err.println("Invalid input");
        else
            CompletableFuture.supplyAsync(() -> socketClientService.updateClient(client)).thenAccept(this::printResponse);
    }

    /**
     * Prints all clients in the repository
     *
     */
    private void printAllClients() {
        CompletableFuture.supplyAsync(socketClientService::getAllClients).thenAccept(this::printResponse);
    }

    private void filterClientsByName() {
        System.out.print("Enter name: ");
        Scanner scannerName = new Scanner(System.in);
        String name = scannerName.nextLine();

        CompletableFuture.supplyAsync(() -> socketClientService.filterClientsByName(name)).thenAccept(this::printResponse);
    }

    private void filterClientsByCnp() {
        System.out.print("Enter CNP: ");
        Scanner scannerName = new Scanner(System.in);
        String cnp = scannerName.nextLine();

        CompletableFuture.supplyAsync(() -> socketClientService.filterClientsByCnp(cnp)).thenAccept(this::printResponse);
    }

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
            return null;
        }
    }

    /**
     * Reads a client ID from the keyboard.
     *
     * @return an {@code Long} - null if the data was not valid, otherwise returns the ID.
     */
    private String readClientID() {
        System.out.println("Read Client ID {id}");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            return bufferRead.readLine();
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * Reads a client from the keyboard for update.
     *
     * @return an {@code Client} - null if the data was not valid, otherwise returns the entity.
     */
    private String readClientUpdate() {
        System.out.println("Read Client {ID, CNP, Name, Email, Address}");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            String id = bufferRead.readLine();
            String cnp = bufferRead.readLine();
            String name = bufferRead.readLine();
            String email = bufferRead.readLine();
            String address = bufferRead.readLine();

            return id + ";" + cnp + ";" + name + ";" + email + ";" + address;
        } catch (IOException e) {
            return null;
        }
    }

//`````````````````````````````````````````````````Subscription`````````````````````````````````````````````````//
    /**
     * Adds the Subscription that has been read from console
     */
    private void addSubscription() {
        String subscription = readSubscription();
        if (subscription == null)
            System.err.println("Please insert valid data");

        else
            CompletableFuture.supplyAsync(() -> socketSubscriptionService.addSubscription(subscription)).thenAccept(this::printResponse);
    }

    /**
     * Deletes a subscription by an ID that has been read from console.
     *
     */
    private void deleteSubscription() {
        String id = readSubscriptionID();
        if (id == null) {
            System.err.println("Invalid ID");
        }
        else
            CompletableFuture.supplyAsync(() -> socketSubscriptionService.deleteSubscription(id)).thenAccept(this::printResponse);
    }

    /**
     * Update the Subscription that has been read from console
     */
    private void updateSubscription() {
        String subscription = readSubscriptionUpdate();
        if (subscription == null) {
            System.err.println("Invalid input");
        }
        CompletableFuture.supplyAsync(() -> socketSubscriptionService.updateSubscription(subscription)).thenAccept(this::printResponse);
    }

    /**
     * Prints all subscriptions in the repository
     *
     */
    private void printAllSubscriptions() {
        CompletableFuture.supplyAsync(socketSubscriptionService::getAllSubscriptions).thenAccept(this::printResponse);
    }

    private void filterSubscriptionsByDuration() {
        System.out.print("Enter duration: ");
        Scanner scannerDuration = new Scanner(System.in);
        String duration = scannerDuration.nextLine();

        CompletableFuture.supplyAsync(() -> socketSubscriptionService.filterSubscriptionByDuration(duration)).thenAccept(this::printResponse);
    }

    private void filterSubscriptionsByType() {
        System.out.print("Enter type: ");
        Scanner scannerType = new Scanner(System.in);
        String type = scannerType.nextLine();

        CompletableFuture.supplyAsync(() -> socketSubscriptionService.filterSubscriptionByType(type)).thenAccept(this::printResponse);
    }

    /**
     * Reads a subscription from the keyboard.
     *
     * @return an {@code Subscription} - null if the data was not valid, otherwise returns the entity.
     */
    private String readSubscription() {
        System.out.println("Read Subscription {Type, Price, Duration}");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            String type = bufferRead.readLine();
            String price = bufferRead.readLine();
            String duration = bufferRead.readLine();

            return type + ";" + price + ";" + duration;
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Reads a subscription from the keyboard for update.
     *
     * @return an {@code Subscription} - null if the data was not valid, otherwise returns the entity.
     */
    private String readSubscriptionUpdate() {
        System.out.println("Read Subscription {id, Type, Price, Duration}");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            String id = bufferRead.readLine();
            String type = bufferRead.readLine();
            String price = bufferRead.readLine();
            String duration = bufferRead.readLine();

            return id + ";" + type + ";" + price + ";" + duration;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Reads a subscription ID from the keyboard.
     *
     * @return an {@code Long} - null if the data was not valid, otherwise returns the ID.
     */
    private String readSubscriptionID() {
        System.out.println("Read Subscription ID {id}");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            return bufferRead.readLine();
        }
        catch (IOException e) {
            return null;
        }
    }

//`````````````````````````````````````````````````Contract`````````````````````````````````````````````````//
    /**
     * Adds the contract that has been read from console
     */
    private void addContract() {
        String contract = readContract();
        if (contract == null)
            System.err.println("Please insert valid data");
        else
            CompletableFuture.supplyAsync(() -> socketContractService.addContract(contract)).thenAccept(this::printResponse);
    }

    /**
     * Deletes a contract by an ID that has been read from console.
     *
     */
    private void deleteContract() {
        String id = readContractID();
        if (id == null)
            System.err.println("Invalid ID");
        else
            CompletableFuture.supplyAsync(() -> socketContractService.deleteContract(id)).thenAccept(this::printResponse);
    }

    /**
     * Updates the contract that has been read from console
     */
    private void updateContract() {
        String contract = readContractForUpdate();
        if (contract == null)
            System.err.println("Invalid input");
        else
            CompletableFuture.supplyAsync(() -> socketContractService.updateContract(contract)).thenAccept(this::printResponse);
    }


    /**
     * Prints all contracts in the repository
     *
     */
    private void printAllContracts() {
        CompletableFuture.supplyAsync(socketContractService::getAllContracts).thenAccept(this::printResponse);
    }

    private void filterExpiredContracts() {
        CompletableFuture.supplyAsync(socketContractService::filterActiveContracts).thenAccept(this::printResponse);
    }

    /**
     * Reads a contract from the keyboard.
     *
     * @return an {@code Contract} - null if the data was not valid, otherwise returns the entity.
     */
    private String readContract() {
        System.out.println("Read Contract {Client ID, Subscription ID, Date<yyyy-mm-dd>}");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            String clientId = bufferRead.readLine();
            String subscriptionId = bufferRead.readLine();
            String date = bufferRead.readLine();
            return clientId+";"+subscriptionId+";"+date;
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Reads a contract ID from the keyboard.
     *
     * @return an {@code Long} - null if the data was not valid, otherwise returns the ID.
     */
    private String readContractID() {
        System.out.println("Read Contract ID {id}");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            return bufferRead.readLine();
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * Reads a contract ID and a client ID from the keyboard (because the client is the only field that is allowed to be modified).
     *
     * @return an {@code Contract} - null if the data was not valid, otherwise returns the entity.
     */
    private String readContractForUpdate() {
        System.out.println("Update Contract {id, Client ID}");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            String id = bufferRead.readLine();
            String clientId = bufferRead.readLine();
            return id+";"+clientId;

        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void printResponse(Future<String> response){
        try {
            System.out.println(response.get().replaceAll(";", "\n"));
            System.out.println("******************************");
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Something went wrong with the connection");
        }
    }
}
