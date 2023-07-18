package antifraud.util;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = UserRoleConstraintValidator.class)
@Documented
@Repeatable(UserRoleConstraint.List.class)
public @interface UserRoleConstraint {

    String message() default "{invalid role}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    RoleSet[] acceptedRoles();

    @Target(FIELD)
    @Retention(RUNTIME)
    @Documented
    @interface List {
        UserRoleConstraint[] value();
    }
}


class UserRoleConstraintValidator implements ConstraintValidator<UserRoleConstraint, String> {
    private List<String> acceptedRoles;
    @Override
    public void initialize(UserRoleConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        acceptedRoles = new ArrayList<>();
        var roles = constraintAnnotation.acceptedRoles();
        for (var role : roles) {
            acceptedRoles.add(role.name().toUpperCase());
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return acceptedRoles.contains(value.toUpperCase());
    }
}