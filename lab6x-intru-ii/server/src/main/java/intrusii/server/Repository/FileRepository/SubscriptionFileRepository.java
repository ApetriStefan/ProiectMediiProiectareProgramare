package intrusii.server.Repository.FileRepository;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import intrusii.common.Domain.Subscription;
import intrusii.common.Domain.SubscriptionType;
import intrusii.common.Domain.Validators.Validator;


public class SubscriptionFileRepository extends FileRepository<Long, Subscription> {
    private String fileName;
    private static Long idGenerator = 0L;

    public SubscriptionFileRepository(Validator<Subscription> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        super.loadData();
    }

    @Override
    public Path getPath() {
        return Paths.get(fileName);
    }

    @Override
    public Optional<Subscription> createEntity(List<String> items) {
        Long id = Long.valueOf(items.get(0));
        SubscriptionType type = SubscriptionType.Default;
        String typeString = items.get(1);
        type = type.setSubscriptionType(typeString);
        float price = Float.parseFloat(items.get(2));
        int duration = Integer.parseInt(items.get(3));
        if (id > idGenerator)
            idGenerator = id;
        Subscription subscription = new Subscription(type, price, duration);
        subscription.setId(id);

        return Optional.of(subscription);

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
    public void write(BufferedWriter bufferedWriter, Subscription subscription) {
        try {
            bufferedWriter.write(subscription.getId() + ";" + subscription.getType() + ";" + subscription.getPrice() + ";" + subscription.getDuration());
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
