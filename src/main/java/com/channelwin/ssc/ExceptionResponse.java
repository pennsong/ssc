package com.channelwin.ssc;

public class ExceptionResponse {
    private String message;

    public ExceptionResponse(String message) {
        this.message = message;
    }

    public String toJSON() {
        return "{\"code\": 1 }";
    }
}
