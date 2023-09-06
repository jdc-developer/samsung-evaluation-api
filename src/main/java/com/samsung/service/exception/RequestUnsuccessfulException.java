package com.samsung.service.exception;

public class RequestUnsuccessfulException extends RuntimeException {

    private static final String MESSAGE = "An error occurred while processing this request. Please, try again.";

    public RequestUnsuccessfulException() {
        super(MESSAGE);
    }

    public RequestUnsuccessfulException(String msg) {
        super(msg);
    }

}
