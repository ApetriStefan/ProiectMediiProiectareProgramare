package intrusii.server.Utility;

import intrusii.common.SocketException;
import intrusii.server.Domain.Client;

import java.util.Set;

public class ClientUtil {

    public static Client StringToClient(String client) {
        try {
            String[] arguments = client.split(";");
            String cnp = arguments[0];
            String name = arguments[1];
            String email = arguments[2];
            String address = arguments[3];

            return new Client(cnp, name, email, address);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new SocketException("Something went wrong! Not enough attributes");
        }
    }

    public static Client StringToClientWithId(String client) {
        try {
            String[] arguments = client.split(";");
            String id = arguments[0];
            String cnp = arguments[1];
            String name = arguments[2];
            String email = arguments[3];
            String address = arguments[4];

            Long idLong = Long.parseLong(id);

            Client clientObj = new Client(cnp, name, email, address);
            clientObj.setId(idLong);
            return clientObj;
        } catch (ArrayIndexOutOfBoundsException |  NumberFormatException e) {
            throw new SocketException("Something went wrong! Not enough attributes");
        }
    }

    public static String SetToString(Set<Client> clients){
        return clients.stream().map(Client::toString).reduce("", (clientsString, client) -> clientsString + ";" + client);
    }
}
