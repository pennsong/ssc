package com.channelwin.ssc.QuestionWarehouse.model.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = TestFieldValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface TestFieldConstraint {
    String message() default "字段没通过校验!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
