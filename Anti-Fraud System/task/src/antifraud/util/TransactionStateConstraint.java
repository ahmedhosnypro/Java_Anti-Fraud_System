package antifraud.util;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = TransactionStateConstraintValidator.class)
@Documented
@Repeatable(TransactionStateConstraint.List.class)
public @interface TransactionStateConstraint {

    String message() default "{invalid role}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target(FIELD)
    @Retention(RUNTIME)
    @Documented
    @interface List {
        TransactionStateConstraint[] value();
    }
}


class TransactionStateConstraintValidator implements ConstraintValidator<TransactionStateConstraint, String> {

    @Override
    public void initialize(TransactionStateConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return TransactionState.getEntries().stream().map(Enum::name).toList().contains(value);
    }
}