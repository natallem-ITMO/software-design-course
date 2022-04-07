package errors;

public class NoValidVisitException extends RuntimeException {
    public NoValidVisitException(String errorMessage) {
        super(errorMessage);
    }
}
