package com.egs.bank.controller;

import com.egs.bank.model.domain.AuthRequest;
import com.egs.bank.model.dto.AuthTypesDTO;
import com.egs.bank.model.dto.TokenDTO;
import com.egs.bank.service.AuthenticateService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/auth")
@Api(tags = "authentication apis")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticateService authenticateService;

    @GetMapping("/types")
    public AuthTypesDTO getAuthenticationsMechanisms(){
        return authenticateService.getAuthMechanisms();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TokenDTO authenticate(@Validated @RequestBody AuthRequest authRequest){
        return authenticateService.authenticate(authRequest);
    }
}
