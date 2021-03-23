package intrusii.server.Utility;

import intrusii.common.SocketException;
import intrusii.server.Domain.Client;
import intrusii.server.Domain.Contract;
import intrusii.server.Domain.Subscription;
import intrusii.server.Domain.SubscriptionType;

import java.time.LocalDate;
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
            throw new SocketException("Something went wrong! Check the input");
        }
    }

    public static Contract StringToContractWithId(String contract) {
        try {
            String[] arguments = contract.split(";");
            String id = arguments[0];
            String clientId = arguments[1];
            String subscriptionId = arguments[2];
            String date = arguments[3];

            Long idLong = Long.parseLong(id);
            Contract contractObj = new Contract(Long.valueOf(clientId), Long.valueOf(subscriptionId), LocalDate.parse(date));
            contractObj.setId(idLong);
            return contractObj;
        } catch (ArrayIndexOutOfBoundsException |  NumberFormatException e) {
            throw new SocketException("Something went wrong! Check the input");
        }
    }

    public static String SetToString(Set<Contract> contracts)
    {
        return contracts.stream().map(Contract::toString).reduce("", (contractsString, contract) -> contractsString + ";" + contract);
    }
}
