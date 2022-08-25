package com.egs.bank.intrface;

import com.egs.bank.model.domain.TransactionResult;

import java.util.List;

public interface ITransaction {
    List<Integer> getPredefinedValues();

    TransactionResult withdraw(Integer value, String userId);

    TransactionResult deposit(Integer value, String userId);

    TransactionResult rollback(String transactionId);

    long getUserBalance(String userId);
}
