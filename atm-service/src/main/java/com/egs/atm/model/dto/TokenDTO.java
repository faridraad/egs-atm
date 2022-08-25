package com.egs.atm.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenDTO {
    private String token;
}
