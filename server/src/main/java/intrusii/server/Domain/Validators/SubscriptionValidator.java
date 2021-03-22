package intrusii.server.Domain.Validators;

import intrusii.server.Domain.Subscription;
import intrusii.server.Domain.SubscriptionType;

import java.util.Optional;


public class SubscriptionValidator implements Validator<Subscription> {
    /**
     * Validates the given Subscription (Subscription entity)
     *
     * @param entity
     *            must be not null.
     *
     * @throws ValidatorException
     *             if the given duration is not a positive value.
     *             if the given price is not a positive value.
     *             if the given type is Default.
     */
    @Override
    public void validate(Subscription entity) throws ValidatorException {
        Optional.of(entity.getDuration()).filter(duration -> duration > 0).orElseThrow(() -> new ValidatorException("Invalid duration"));
        Optional.of(entity.getPrice()).filter(price -> price > 0).orElseThrow(() -> new ValidatorException("Invalid price"));
        Optional.of(entity.getType()).filter(type -> !type.equals(SubscriptionType.Default)).orElseThrow(() -> new ValidatorException("Invalid type"));
    }
}
