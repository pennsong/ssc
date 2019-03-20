package com.channelwin.ssc;

import com.channelwin.ssc.QuestionWarehouse.model.validator.Group;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Slf4j
@Aspect
@Component
public class DtoValidateAOP {

    @Pointcut("execution(* com.channelwin.ssc.*.controller.*.edit*(..)) || execution(* com.channelwin.ssc.*.controller.*.add*(..))")
    public void addOrEdit(){}

    @Before("addOrEdit()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        for (Object item: joinPoint.getArgs()) {
            if (item instanceof Validatable) {
                Set<ConstraintViolation<Validatable>> violations = validator.validate((Validatable)item, Group.class);
                if (violations.size() > 0) {
                    throw new ValidateException(violations.toString());
                }
            }
        }
    }
}