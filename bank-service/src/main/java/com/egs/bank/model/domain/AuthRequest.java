package com.egs.bank.model.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class AuthRequest {
    @ApiModelProperty(example = "'1122334455667788'", required = true)
    @NotBlank(message = "cardNumber can not be blank")
    private String cardNumber;
    @ApiModelProperty(example = "'FINGERPRINT'", required = true)
    @NotNull(message = "authType can not be null")
    private AuthenticationMechanism authType;
    @ApiModelProperty(example = "'qwertyuiopl'", required = true)
    @NotBlank(message = "authType can not be blank")
    private String value;
}
