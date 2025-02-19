package antifraud.util;


import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = CardNumberConstraintValidator.class)
@Documented
@Repeatable(CardNumberConstraint.List.class)
public @interface CardNumberConstraint {

    String message() default "{invalid card number}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target(FIELD)
    @Retention(RUNTIME)
    @Documented
    @interface List {
        CardNumberConstraint[] value();
    }
}


class CardNumberConstraintValidator implements ConstraintValidator<CardNumberConstraint, String> {
    @Override
    public void initialize(CardNumberConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return CardNumberValidator.INSTANCE.isValid(value);
    }
}