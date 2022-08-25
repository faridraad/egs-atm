package com.egs.atm.model.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Builder
@Data
public class AuthenticationTypesDTO {
    private List<String> strategies;

}
