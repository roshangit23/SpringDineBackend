package com.hotelPro.hotelmanagementsystem.controller;

public class ApiResponse<T> {
    private int status;
    private T doc;

    public ApiResponse(int status, T doc) {
        this.status = status;
        this.doc = doc;
    }

    // getters and setters

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getDoc() {
        return doc;
    }

    public void setDoc(T doc) {
        this.doc = doc;
    }
}
