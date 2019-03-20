package com.channelwin.ssc.QuestionWarehouse.model.validator;

import com.channelwin.ssc.Validatable;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class TypeValidator implements ConstraintValidator<TypeConstraint, Validatable> {
    @Override
    public boolean isValid(Validatable value, ConstraintValidatorContext context) {
        log.info("pptest TypeConstraint");
        try {
            value.validate();
        } catch (Exception e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
