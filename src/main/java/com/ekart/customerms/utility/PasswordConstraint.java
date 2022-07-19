package com.ekart.customerms.utility;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {

	String message() default "Password must contain lowercase, uppercase, digit and special character and must be greater than 8 characters and less than 15";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
