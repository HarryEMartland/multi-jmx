package uk.co.harrymartland.multijmx.validator;

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, ClassNotFoundException e) {
        super(message, e);
    }
}
