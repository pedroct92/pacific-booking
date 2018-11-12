package com.challenge.pedrotorres.pacificbooking.commons.exceptions;

public class ValidationException extends RuntimeException {

    private Integer status;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Integer status) {
        super(message);
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
