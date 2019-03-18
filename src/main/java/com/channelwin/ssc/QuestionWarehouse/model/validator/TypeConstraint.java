package com.channelwin.ssc.QuestionWarehouse.model.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = TypeValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface TypeConstraint {
    String message() default "实体没通过校验!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
