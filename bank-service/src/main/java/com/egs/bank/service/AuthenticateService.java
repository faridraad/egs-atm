package com.egs.bank.service;

import com.egs.bank.configuration.resources.ApplicationProperties;
import com.egs.bank.intrface.IAuthenticator;
import com.egs.bank.model.domain.AuthRequest;
import com.egs.bank.model.domain.AuthenticationMechanism;
import com.egs.bank.model.dto.AuthTypesDTO;
import com.egs.bank.model.dto.TokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthenticateService  {

    private final List<IAuthenticator> authenticatorList;
    private final ApplicationProperties applicationProperties;
    public TokenDTO authenticate(AuthRequest authRequest) {
        String token = this.getAuthMechanism(authRequest.getAuthType())
                .authenticate(authRequest.getCardNumber(), authRequest.getValue());
        return new TokenDTO(token);
    }

    public AuthTypesDTO getAuthMechanisms() {
        List<String> mechanisms = Arrays.stream(AuthenticationMechanism.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return new AuthTypesDTO(mechanisms);
    }

    public IAuthenticator getAuthMechanism(AuthenticationMechanism authType) {
        for (IAuthenticator authenticator : authenticatorList) {
            if (authenticator.matches(authType))
                return authenticator;
        }
        throw new IllegalArgumentException(applicationProperties.getProperty("message.fail.attempt.finger.print.text"));
    }
}
