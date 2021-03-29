package intrusii.server.Repository.FileRepository;

import intrusii.common.Domain.Client;
import intrusii.common.Domain.Validators.Validator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;


public class ClientFileRepository extends FileRepository<Long, Client> {
    private String fileName;
    private static Long idGenerator = 0L;

    public ClientFileRepository(Validator<Client> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        super.loadData();
    }

    @Override
    public Path getPath() {
        return Paths.get(fileName);
    }

    @Override
    public Optional<Client> createEntity(List<String> items) {
        Long id = Long.valueOf(items.get(0));
        String cnp =  items.get(1);
        String name = items.get(2);
        String email = items.get(3);
        String address = items.get(4);
        if (id > idGenerator)
            idGenerator = id;

        Client c = new Client(cnp, name, email, address);
        c.setId(id);
        return Optional.of(c);

    }

    @Override
    public Long getNextId() {
        return idGenerator++;
    }

    @Override
    public void increaseID(){
        idGenerator++;
    }

    @Override
    public void decreaseID() {
        idGenerator--;
    }

    @Override
    public void write(BufferedWriter bufferedWriter, Client client) {
        try {
            bufferedWriter.write(client.getId() + ";" + client.getCnp() + ";" + client.getName() + ";" + client.getEmail()+";"+client.getAddress());
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}