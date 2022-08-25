package com.egs.atm.model.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CardAuthenticationRequest {
    private String cardNumber;
    private String authType;
    private String value;
}
