package ru.akirakozov.sd.app.errors;

public class ClientRuntimeException extends RuntimeException {
    public ClientRuntimeException(String errorMessage) {
        super(errorMessage);
    }
}