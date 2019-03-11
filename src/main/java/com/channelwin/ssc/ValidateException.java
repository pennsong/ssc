package com.channelwin.ssc;

import org.springframework.validation.ObjectError;

import java.util.List;

public class ValidateException extends RuntimeException{
    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(List<ObjectError> errorList) {
        super("");
    }
}
