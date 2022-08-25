package com.egs.bank.intrface;

import com.egs.bank.configuration.exception.NotAuthenticatedException;
import com.egs.bank.configuration.exception.NumberOfAttemptsExceededException;
import com.egs.bank.model.domain.AuthenticationMechanism;
import com.egs.bank.model.domain.Failure;

import java.util.function.Supplier;

public interface IAuthenticator {
    /**
     * could have multiple implementation
     * @param input is everyThing that we decided to authenticate with
     * @param cardNumber
     * @return  generated token with expiration mechanism
     */
    String authenticate(String cardNumber, String input);

    /**
     * defines the implementation strategy is fitted or not
     * @param authenticationMechanism
     * @return
     */
    boolean matches(AuthenticationMechanism authenticationMechanism);

    default RuntimeException doFailureWithMessageSupply(Supplier<Failure> failureSupplier){
        Failure failure = failureSupplier.get();
        if (failure.getNumberOfAttempts() > 3)
            return new NumberOfAttemptsExceededException(failure.getMessage());
        return new NotAuthenticatedException(failure.getMessage());
    }
}
