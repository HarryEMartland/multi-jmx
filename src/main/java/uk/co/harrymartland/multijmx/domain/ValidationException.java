package uk.co.harrymartland.multijmx.domain;

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Exception e) {
        super(message, e);
    }
}
