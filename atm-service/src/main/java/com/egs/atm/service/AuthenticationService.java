package com.egs.atm.service;

import com.egs.atm.configuration.exception.FeignCustomException;
import com.egs.atm.configuration.exception.TooManyRequestCustomException;
import com.egs.atm.configuration.resources.ApplicationProperties;
import com.egs.atm.intrface.IAuthentication;
import com.egs.atm.feign.IAuthenticationRestApi;
import com.egs.atm.model.domain.CardAuthenticationRequest;
import com.egs.atm.model.dto.AuthenticationTypesDTO;
import com.egs.atm.model.dto.TokenDTO;
import com.egs.atm.utility.ExceptionConverter;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationService implements IAuthentication {

    private final IAuthenticationRestApi bankAuthApiClient;
    private final ApplicationProperties applicationProperties;

    @Override
    public AuthenticationTypesDTO getAuthTypes() {
        return bankAuthApiClient.getAuthMechanisms();
    }

    @CircuitBreaker(name = "bank-auth", fallbackMethod = "getFreshTokenFallBack")
    @Override
    public TokenDTO getFreshToken(CardAuthenticationRequest authRequest) {
        return bankAuthApiClient.getToken(authRequest);
    }
    // Used at CircuitBreaker
    private TokenDTO getFreshTokenFallBack(Throwable throwable){
        RuntimeException exception = getProperExceptionFromCoreBankException(throwable);
        if (exception instanceof TooManyRequestCustomException){
            log.warn(applicationProperties.getProperty("log.warn.too.many.request.exception.text"), exception);
        }
        throw exception;
    }

    private RuntimeException getProperExceptionFromCoreBankException(Throwable throwable) {
        if (throwable.getClass().equals(FeignCustomException.class)){
            return ExceptionConverter.getProperExceptionFromFeignGeneralException((FeignCustomException) throwable);
        }else{
            // circuit opened
            return new FeignCustomException.ServiceUnavailable(applicationProperties.getProperty("error.feign.exception.second.try.text"));
        }
    }
}
