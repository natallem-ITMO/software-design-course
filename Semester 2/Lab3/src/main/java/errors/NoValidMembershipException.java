package errors;

public class NoValidMembershipException extends RuntimeException {
    public NoValidMembershipException(String errorMessage) {
        super(errorMessage);
    }
}
