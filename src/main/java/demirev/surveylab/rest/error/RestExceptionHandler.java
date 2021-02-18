package demirev.surveylab.rest.error;

import demirev.surveylab.rest.exceptions.NotFoundException;
import demirev.surveylab.rest.exceptions.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public String notFound(NotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ValidationException.class})
    public String validationError(ValidationException ex) {
        return ex.getMessage();
    }

}