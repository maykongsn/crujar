package com.spring.crujar.handler;

import com.spring.crujar.exception.BadRequestException;
import com.spring.crujar.exception.BadRequestExceptionDetails;
import com.spring.crujar.exception.ExceptionDetails;
import com.spring.crujar.exception.ValidationExceptionDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handleBadRequestException(BadRequestException exception) {
        return new ResponseEntity<>(
                BadRequestExceptionDetails.builder()
                        .details(exception.getMessage())
                        .developerMessage(exception.getClass().getName())
                        .timestamp(LocalDateTime.now())
                        .title("Bad Request Exception")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build(), HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                ValidationExceptionDetails.builder()
                        .fields(fields)
                        .fieldsMessage(fieldsMessage)
                        .details("Check the fields error")
                        .developerMessage(exception.getClass().getName())
                        .timestamp(LocalDateTime.now())
                        .title("Bad Request Exception, invalid fields")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build(), HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .title(ex.getCause().getMessage())
                .status(status.value())
                .build();
        return new ResponseEntity<>(exceptionDetails, headers, status);
    }
}
