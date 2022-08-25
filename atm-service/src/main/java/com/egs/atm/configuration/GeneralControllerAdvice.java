package com.egs.atm.configuration;

import com.egs.atm.configuration.exception.*;
import com.egs.atm.configuration.resources.ApplicationProperties;
import com.egs.atm.model.domain.ValidationError;
import com.egs.atm.model.dto.ErrorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
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

    private final ObjectMapper objectMapper;
    private final ApplicationProperties applicationProperties;
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ErrorDTO methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.warn(applicationProperties.getProperty("log.warn.core.bank.method.argument.exception.text"), e);
        Set<ValidationError> validationErrorSet = new HashSet<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            if (!validationErrorSet.add(
                    ValidationError.builder()
                            .field(fieldError.getField())
                            .message(fieldError.getDefaultMessage() == null ? fieldError.getCode() : fieldError.getDefaultMessage())
                            .build()
            )) {
                throw new IllegalStateException(applicationProperties.getProperty("application.message.duplicate.key.text"));
            }
        }
        return ErrorDTO.builder().errors(validationErrorSet).build();
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ErrorDTO bindExceptionHandler(BindException e) {
        log.warn("bindExceptionHandler", e);
        Set<ValidationError> errorSet = new HashSet<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            if (!errorSet.add(
                    ValidationError.builder()
                            .field(fieldError.getField())
                            .message(fieldError.getDefaultMessage() == null ? fieldError.getCode() : fieldError.getDefaultMessage())
                            .build()
            )) {
                throw new IllegalStateException(applicationProperties.getProperty("application.message.duplicate.key.text"));
            }
        });
        return ErrorDTO.builder().errors(errorSet).build();

    }

    @SneakyThrows
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO validationExceptionHandler(ValidationException e){
        return objectMapper.readValue(e.getMessage(), ErrorDTO.class);
    }

    @SneakyThrows
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.PROXY_AUTHENTICATION_REQUIRED)
    @ResponseBody
    public ErrorDTO authenticationFailedExceptionHandler(AuthenticationException e){
        String message = Strings.isBlank(e.getMessage()) ? "{}" : e.getMessage();
        return objectMapper.readValue(message, ErrorDTO.class);
    }

    @SneakyThrows
    @ExceptionHandler(TooManyRequestCustomException.class)
    @ResponseStatus(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
    @ResponseBody
    public ErrorDTO tooManyRequestsExceptionHandler(TooManyRequestCustomException e){
        return objectMapper.readValue(e.getMessage(), ErrorDTO.class);
    }

    @ExceptionHandler(FeignCustomException.ServiceUnavailable.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public ErrorDTO serviceUnavailableExceptionHandler(FeignCustomException.ServiceUnavailable e){
        log.warn(applicationProperties.getProperty("log.warn.core.bank.unavailable.text"), e);
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
