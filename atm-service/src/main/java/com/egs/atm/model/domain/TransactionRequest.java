package com.egs.atm.model.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TransactionRequest {
    private Integer value;
}
