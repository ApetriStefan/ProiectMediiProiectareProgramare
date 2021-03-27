package intrusii.server.Repository.FileRepository;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import intrusii.server.Domain.Contract;
import intrusii.server.Domain.Validators.Validator;


public class ContractFileRepository extends FileRepository<Long, Contract> {
    private String fileName;
    private static Long idGenerator = 0L;

    public ContractFileRepository(Validator<Contract> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        super.loadData();
    }

    @Override
    public Path getPath() {
        return Paths.get(fileName);
    }

    @Override
    public Optional<Contract> createEntity(List<String> items) {
        Long id = Long.valueOf(items.get(0));
        Long clientId = Long.parseLong(items.get(1));
        Long subscriptionId = Long.parseLong(items.get((2)));
        LocalDate date = LocalDate.parse(items.get(3));
        Contract contract = new Contract(clientId, subscriptionId, date);
        contract.setId(id);
        if (id > idGenerator)
            idGenerator = id;

        Contract c = new Contract(clientId, subscriptionId, date);
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
    public void write(BufferedWriter bufferedWriter, Contract contract) {
        try {
            bufferedWriter.write(contract.getId() + ";" + contract.getClientId() + ";" + contract.getSubscriptionId() + ";" + contract.getDate());
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
