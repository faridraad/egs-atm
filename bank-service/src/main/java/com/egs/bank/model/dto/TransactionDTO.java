package com.egs.bank.model.dto;

import lombok.Data;

@Data
public class TransactionDTO {
    private Integer value;
    private Long balance;
    private String transactionId;
}
