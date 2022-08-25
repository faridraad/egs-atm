package com.egs.bank.model.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserQuery {
    private String pin;
    private String card;
    private String fingerprint;
}
