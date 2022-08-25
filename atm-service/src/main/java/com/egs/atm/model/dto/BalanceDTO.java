package com.egs.atm.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BalanceDTO {
    private long balance;
}
