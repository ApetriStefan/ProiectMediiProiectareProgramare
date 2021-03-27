package intrusii.server.Repository.FileRepository;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import intrusii.server.Domain.BaseEntity;
import intrusii.server.Domain.Validators.ContractException;
import intrusii.server.Domain.Validators.Validator;
import intrusii.server.Domain.Validators.ValidatorException;
import intrusii.server.Repository.InMemoryRepository;

public abstract class FileRepository<ID, T extends BaseEntity<ID>> extends InMemoryRepository<ID, T> {

    public FileRepository(Validator<T> validator) {
        super(validator);
    }

    public Optional<T> createEntity(List<String> items) {
        return Optional.empty();
    }

    public void increaseID(){
    }

    public Path getPath() {
        return null;
    }

    public void loadData() {
        Path path = getPath();
        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split(";"));
                Optional<T> entity = createEntity(items);
                try {
                    super.save(entity.get());
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        increaseID();
    }

    public ID getNextId() {
        return null;
    }

    @Override
    public Optional<T> save(T entity) throws ValidatorException {
        entity.setId(getNextId());
        Optional<T> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        writeToFile();
        return Optional.empty();
    }

    public void decreaseID(){
    }

    /**
     * Deletes the given entity by ID (Long ID), and updates the file
     *
     * @param id
     *            must not be null.
     * @return an {@code Optional} - the entity if the entity was deleted otherwise returns the null.
     * @throws ContractException
     *             if the given id is not valid.
     */
    @Override
    public Optional<T> delete(ID id) {
        try {
            decreaseID();
            Optional<T> entity = super.delete(id);
            writeToFile();
            return entity;
        }
        catch(RuntimeException ex) {
            throw new ContractException(ex);
        }
    }

    /**
     * Updates the given entity, and updates the file
     *
     * @param entity
     *            must not be null.
     * @return an {@code Optional} - the entity if the entity was updated otherwise returns the null.
     * @throws ContractException
     *             if the given id is not valid.
     */
    @Override
    public Optional<T> update(T entity) throws ContractException {
        try {
            Optional<T> e = super.update(entity);
            writeToFile();
            return e;
        }
        catch(RuntimeException ex) {
            throw new ContractException(ex);
        }
    }

    public void write(BufferedWriter bufferedWriter, T entity) {
    }

    /**
     * Writes the current in memory repository to the file
     *
     */
    private void writeToFile() {
        Path path = getPath();
        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(path))
        {
            super.findAll().forEach(entity -> {
                write(bufferedWriter, entity);
            });
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
