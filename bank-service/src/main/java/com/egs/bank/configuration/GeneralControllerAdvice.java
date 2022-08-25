package com.egs.bank.configuration;

import com.egs.bank.configuration.exception.*;
import com.egs.bank.configuration.resources.ApplicationProperties;
import com.egs.bank.model.dto.ErrorDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GeneralControllerAdvice {

    private final ApplicationProperties applicationProperties;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ErrorDTO methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.warn(applicationProperties.getProperty("log.warn.core.bank.method.argument.exception.text"), e);
        Set<ErrorDTO.ValidationError> validationErrorSet = new HashSet<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            if (!validationErrorSet.add(
                    ErrorDTO.ValidationError.builder()
                            .field(fieldError.getField())
                            .message(fieldError.getDefaultMessage() == null ? fieldError.getCode() : fieldError.getDefaultMessage())
                            .build()
            )) {
                throw new IllegalStateException(applicationProperties.getProperty("application.message.duplicate.key.text"));
            }
        }
        return ErrorDTO.builder().errors(validationErrorSet).build();
    }
// the same
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ErrorDTO bindExceptionHandler(BindException e) {
        log.warn("bindExceptionHandler", e);
        Set<ErrorDTO.ValidationError> errorSet = new HashSet<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            if (!errorSet.add(
                    ErrorDTO.ValidationError.builder()
                            .field(fieldError.getField())
                            .message(fieldError.getDefaultMessage() == null ? fieldError.getCode() : fieldError.getDefaultMessage())
                            .build()
            )) {
                throw new IllegalStateException(applicationProperties.getProperty("application.message.duplicate.key.text"));
            }
        });
        return ErrorDTO.builder().errors(errorSet).build();

    }

    @ExceptionHandler(NotAuthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorDTO notAuthenticatedExceptionHandler(NotAuthenticatedException e) {
        log.warn(applicationProperties.getProperty("log.warn.not.authenticated.exception.text"), e);
        return ErrorDTO.builder().message(e.getMessage()).build();
    }

    @ExceptionHandler(NumberOfAttemptsExceededException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ResponseBody
    public ErrorDTO numberOfAttemptsExceededExceptionHandler(NumberOfAttemptsExceededException e) {
        log.warn(applicationProperties.getProperty("log.warn.attempts.exceeded.exception.text"), e);
        return ErrorDTO.builder().message(e.getMessage()).build();
    }

    @ExceptionHandler(PreconditionFailedException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ResponseBody
    public ErrorDTO preconditionFailedExceptionHandler(PreconditionFailedException e){
        log.warn(applicationProperties.getProperty("log.warn.precondition.exception.text"), e);
        return ErrorDTO.builder().message(e.getMessage()).build();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorDTO conflictExceptionHandler(ConflictException e){
        log.warn(applicationProperties.getProperty("log.warn.conflict.exception.text"), e);
        return ErrorDTO.builder().message(e.getMessage()).build();
    }
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO notFoundExceptionHandler(NotFoundException e){
        log.warn(applicationProperties.getProperty("log.warn.not.found.exception.text"), e);
        return ErrorDTO.builder().message(e.getMessage()).build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO generalErrorHandler(Exception e) {
        String fingerPrint = UUID.randomUUID().toString();
        log.error(applicationProperties.getProperty("log.error.core.bank.finger.print.exception.text"), fingerPrint, e);
        return ErrorDTO.builder().logFingerPrint(fingerPrint).build();
    }
}

