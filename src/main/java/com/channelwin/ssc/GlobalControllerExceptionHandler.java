package com.channelwin.ssc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorResponse handleBadRequest(HttpServletRequest req, Exception ex) {
        log.info(ex.toString());
        for (StackTraceElement item: ex.getStackTrace()) {
            log.info("[" + item.getClassName() + ": " + item.getLineNumber() + "]: " + item.toString());
        }

        return new ErrorResponse(ex.getLocalizedMessage());
    }
}
