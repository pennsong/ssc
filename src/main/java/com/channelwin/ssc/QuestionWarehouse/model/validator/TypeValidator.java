package com.channelwin.ssc.QuestionWarehouse.model.validator;

import com.channelwin.ssc.QuestionWarehouse.model.Validatable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TypeValidator implements ConstraintValidator<TypeConstraint, Validatable> {
    @Override
    public boolean isValid(Validatable value, ConstraintValidatorContext context) {
        try {
            value.validate();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
