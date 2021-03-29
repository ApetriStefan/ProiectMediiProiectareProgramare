package intrusii.common.Domain.Validators;
import intrusii.common.Domain.Client;

import java.util.Optional;

public class ClientValidator implements Validator<Client> {
    /**
     * Validates the given client (Client entity)
     *
     * @param entity
     *            must be not null.
     *
     * @throws ValidatorException
     *             if the given cnp is not a number.
     *             if the given E-mail is not a valid email address.
     */
    @Override
    public void validate(Client entity) throws ValidatorException {
        try {
            Long.parseLong(entity.getCnp());
        } catch (NumberFormatException e) {
            throw new ValidatorException("Invalid CNP");
        }
        Optional.ofNullable(entity.getEmail()).filter(email -> email.contains("@")).orElseThrow(() -> new ValidatorException("Invalid E-Mail address"));
    }
}
