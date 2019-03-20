package com.channelwin.ssc;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class DtoValidateAOP {

    @Pointcut("execution(* com.channelwin.ssc.*.controller.*.edit*(..)) || execution(* com.channelwin.ssc.*.controller.*.add*(..))")
    public void addOrEdit(){}

    @Before("addOrEdit()")
    public void doBefore(JoinPoint joinPoint) {
        for (Object item: joinPoint.getArgs()) {
            if (item instanceof Validatable) {
               ((Validatable) item).validate();
            }
        }
    }
}