package com.hotelPro.hotelmanagementsystem.exception;

public class InsufficientPaymentException extends RuntimeException{
    public InsufficientPaymentException(String message) {
        super(message);
    }
}
