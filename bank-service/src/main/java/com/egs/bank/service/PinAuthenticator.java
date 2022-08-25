package com.egs.bank.service;

import com.egs.bank.configuration.resources.ApplicationProperties;
import com.egs.bank.intrface.IAuthenticator;
import com.egs.bank.intrface.ITokenGranter;
import com.egs.bank.intrface.IUser;
import com.egs.bank.model.domain.AuthenticationMechanism;
import com.egs.bank.model.domain.Failure;
import com.egs.bank.model.domain.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PinAuthenticator implements IAuthenticator {

    private final IUser userService;
    private final ITokenGranter tokenGranter;
    private final ApplicationProperties   applicationProperties;

    @Override
    public String authenticate(String cardNumber, String pin) {
        UserQuery queryModel = UserQuery.builder().card(cardNumber).pin(pin).build();
        return userService.getUserId(queryModel)
                .map(tokenGranter::generateToken)
                .orElseThrow(() -> doFailureProcess(queryModel.getCard()));
    }

    @Override
    public boolean matches(AuthenticationMechanism authenticationMechanism) {
        return AuthenticationMechanism.PIN.equals(authenticationMechanism);
    }


    private  RuntimeException doFailureProcess(String cardNo) {
        int attempts = userService.addTodayFailedLoginAttempts(cardNo);
        return doFailureWithMessageSupply(
                () -> new Failure(attempts, applicationProperties.getProperty("error.authentication.fail.exception.text") + attempts)
        );
    }

}
