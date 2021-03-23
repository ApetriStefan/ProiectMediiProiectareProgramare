package intrusii.server.Utility;

import intrusii.common.SocketException;
import intrusii.server.Domain.Client;
import intrusii.server.Domain.Contract;
import intrusii.server.Domain.Subscription;
import intrusii.server.Domain.SubscriptionType;
import intrusii.server.Service.ClientService;
import intrusii.server.Service.SubscriptionService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

public class ContractUtil {
    public static Contract StringToContract(String contract) {
        try {
            String[] arguments = contract.split(";");
            String clientId = arguments[0];
            String subscriptionId = arguments[1];
            String date = arguments[2];

            return new Contract(Long.valueOf(clientId), Long.valueOf(subscriptionId), LocalDate.parse(date));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new SocketException("Something went wrong! Not enough attributes");
        }
        catch (NumberFormatException e){
            throw new SocketException("Incorrect type for the attributes");
        }
        catch (DateTimeParseException e){
            throw new SocketException("Incorrect date format");
        }
    }

    public static Contract StringToContractWithId(String contract) {
        try {
            String[] arguments = contract.split(";");
            String id = arguments[0];
            String clientId = arguments[1];

            Long idLong = Long.parseLong(id);
            Contract contractObj = new Contract(Long.valueOf(clientId));
            contractObj.setId(idLong);
            return contractObj;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new SocketException("Something went wrong! Check the input");
        }
        catch (NumberFormatException e){
            throw new SocketException("Incorrect type for the attributes");
        }
        catch (DateTimeParseException e){
            throw new SocketException("Incorrect date format");
        }
    }

    public static String SetToString(Set<Contract> contracts, ClientService clientService, SubscriptionService subscriptionService) {
        return contracts.stream().map(contract -> "*" + clientService.getClientByID(contract.getClientId()) + ";" + subscriptionService.getSubscriptionByID(contract.getSubscriptionId()) + ";" + "Date{" +  contract.getDate() + "};").reduce("", (contractString, contract) ->  contractString + ";" + contract);
    }
}
