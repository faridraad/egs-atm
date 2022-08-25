package com.egs.atm.feign;

import com.egs.atm.model.domain.CardAuthenticationRequest;
import com.egs.atm.model.dto.AuthenticationTypesDTO;
import com.egs.atm.model.dto.TokenDTO;
import com.egs.atm.utility.DefaultFeignErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "bank-auth-client", url = "${bank.auth.base.url}", configuration = DefaultFeignErrorDecoder.class)
public interface IAuthenticationRestApi {

    @GetMapping(path = "${bank.auth.getTypes.url}")
    AuthenticationTypesDTO getAuthMechanisms();

    @PostMapping
    TokenDTO getToken(@RequestBody CardAuthenticationRequest authRequest);

}