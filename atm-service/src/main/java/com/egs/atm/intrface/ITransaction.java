package com.egs.atm.intrface;

import com.egs.atm.model.dto.BalanceDTO;
import com.egs.atm.model.dto.PredefinedValueDTO;
import com.egs.atm.model.domain.TransactionRequest;
import com.egs.atm.model.dto.TransactionDTO;

import java.util.List;

public interface ITransaction {
    TransactionDTO withdraw(String token, TransactionRequest transactionRequest);
    TransactionDTO deposit(String token, TransactionRequest transactionRequest);
    TransactionDTO rollback(String token, String transactionId);
    List<PredefinedValueDTO> getPredefinedValues(String token);
    BalanceDTO getBalance(String token);
}
