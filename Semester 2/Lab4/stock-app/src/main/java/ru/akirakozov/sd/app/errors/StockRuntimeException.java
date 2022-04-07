package ru.akirakozov.sd.app.errors;

public class StockRuntimeException extends RuntimeException {
    public StockRuntimeException(String errorMessage) {
        super(errorMessage);
    }
}
