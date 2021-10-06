package pl.gorzki.fmspring;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException ex) {
        List<String> errors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getField() + " - " + x.getDefaultMessage())
                .collect(Collectors.toList());
        return handleError(HttpStatus.BAD_REQUEST, errors, ex);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handle(IllegalArgumentException ex) {
        return handleError(HttpStatus.BAD_REQUEST, List.of(ex.getMessage()), ex);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handle(ConstraintViolationException ex) {
        return handleError(HttpStatus.CONFLICT, List.of(ex.getMessage()), ex);
    }


    private ResponseEntity<Object> handleError(HttpStatus status, List<String> errors, Throwable ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("exception", ex.getClass().getSimpleName());
        body.put("timestamp", new Date());
        body.put("status", status.value());
//get all errors
        body.put("errors", errors);
        return new ResponseEntity<>(body, status);
    }
}
