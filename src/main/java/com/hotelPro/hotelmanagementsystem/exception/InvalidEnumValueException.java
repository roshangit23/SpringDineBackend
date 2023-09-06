package com.hotelPro.hotelmanagementsystem.exception;

public class InvalidEnumValueException extends IllegalArgumentException {
    private final String fieldName;

    public InvalidEnumValueException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}