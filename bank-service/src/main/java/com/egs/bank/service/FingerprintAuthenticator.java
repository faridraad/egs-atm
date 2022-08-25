package com.egs.bank.service;

import com.egs.bank.configuration.resources.ApplicationProperties;
import com.egs.bank.intrface.IAuthenticator;
import com.egs.bank.intrface.ITokenGranter;
import com.egs.bank.intrface.IUser;
import com.egs.bank.model.domain.AuthenticationMechanism;
import com.egs.bank.model.domain.Failure;
import com.egs.bank.model.domain.UserQuery;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FingerprintAuthenticator implements IAuthenticator {

    private final IUser userService;
    private final ITokenGranter tokenGranter;
    private final ApplicationProperties applicationProperties;

    @Override
    public String authenticate(String cardNumber, String fingerprint) {
        UserQuery userQuery = UserQuery.builder().card(cardNumber).fingerprint(fingerprint).build();
        return userService.getUserId(userQuery)
                .map(tokenGranter::generateToken)
                .orElseThrow(() -> doFailureProcess(userQuery.getCard()));
    }

    @Override
    public boolean matches(AuthenticationMechanism authenticationMechanism) {
        return AuthenticationMechanism.FINGERPRINT.equals(authenticationMechanism);
    }


    private  RuntimeException doFailureProcess(String cardNo) {
        int attempts = userService.addTodayFailedLoginAttempts(cardNo);
        return doFailureWithMessageSupply(
                () -> new Failure(attempts, applicationProperties.getProperty("message.fail.attempt.finger.print.text")
                        + attempts)
        );
    }

}
