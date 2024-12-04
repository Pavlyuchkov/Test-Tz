package com.example.ordersservice.exception;

public class NoSuchOrdersException extends RuntimeException {

    public NoSuchOrdersException(String message) {
        super(message);
    }
}