package com.egs.bank.model.domain;

import lombok.Data;

@Data
public class TransactionResult {
    private String transactionId;
    private Long balance;
    private Long value;
}
