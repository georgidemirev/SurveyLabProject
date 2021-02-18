package demirev.surveylab.rest.exceptions;

public class ValidationException extends Exception {
    public ValidationException(String errorMessage) {
        super(errorMessage);
    }
}
