package com.egs.atm.intrface;


import com.egs.atm.model.domain.CardAuthenticationRequest;
import com.egs.atm.model.dto.AuthenticationTypesDTO;
import com.egs.atm.model.dto.TokenDTO;

public interface IAuthentication {

    AuthenticationTypesDTO getAuthTypes();
    TokenDTO getFreshToken(CardAuthenticationRequest cardAuthenticationRequest);
}
