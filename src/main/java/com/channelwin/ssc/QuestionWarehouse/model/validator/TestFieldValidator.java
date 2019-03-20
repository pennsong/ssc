package com.channelwin.ssc.QuestionWarehouse.model.validator;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class TestFieldValidator implements ConstraintValidator<TestFieldConstraint, Double> {
    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        log.info("pptest TestFieldConstraint");

        return true;
    }
}
