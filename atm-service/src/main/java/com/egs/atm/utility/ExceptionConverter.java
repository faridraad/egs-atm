package com.egs.atm.utility;

import com.egs.atm.configuration.exception.AuthenticationException;
import com.egs.atm.configuration.exception.FeignCustomException;
import com.egs.atm.configuration.exception.TooManyRequestCustomException;
import com.egs.atm.configuration.exception.ValidationException;
import com.egs.atm.configuration.resources.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.Objects;

@Slf4j
public class ExceptionConverter {
    ExceptionConverter() {}
    public static RuntimeException getProperExceptionFromFeignGeneralException(FeignCustomException e) {
        log.warn("Exception calling bank services : (ExceptionConverter) ", e);
        HttpStatus status = HttpStatus.resolve(e.getStatus());
        if (Objects.isNull(status))
            return e;
        switch (status) {
            case UNAUTHORIZED:
                return new AuthenticationException(e.getBody());
            case TOO_MANY_REQUESTS:
                return new TooManyRequestCustomException(e.getBody());
            case PRECONDITION_FAILED,
                    CONFLICT,
                    BAD_REQUEST:
                return new ValidationException(e.getBody());
            case NOT_FOUND:
                return new RuntimeException(e.getBody());
            default:
                return new FeignCustomException(e.getStatus(), e.getBody());
        }
    }
}
