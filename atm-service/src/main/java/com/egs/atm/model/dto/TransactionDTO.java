package com.egs.atm.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TransactionDTO {
    private Integer value;
    private Long balance;
    private String transactionId;
}
