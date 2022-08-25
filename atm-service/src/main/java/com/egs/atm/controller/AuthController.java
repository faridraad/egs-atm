package com.egs.atm.controller;

import com.egs.atm.intrface.IAuthentication;
import com.egs.atm.model.domain.CardAuthenticationRequest;
import com.egs.atm.model.dto.AuthenticationTypesDTO;
import com.egs.atm.model.dto.TokenDTO;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "authentication api list")
@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthentication authenticationProxy;

    @GetMapping("/types")
    AuthenticationTypesDTO getAuthTypes(){
        return authenticationProxy.getAuthTypes();
    }

    @PostMapping
    TokenDTO getToken(@RequestBody CardAuthenticationRequest cardAuthenticationRequest){
        return authenticationProxy.getFreshToken(cardAuthenticationRequest);
    }
}
