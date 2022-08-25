package com.egs.bank.model.mapper;


import com.egs.bank.model.dto.TransactionDTO;
import com.egs.bank.model.domain.TransactionResult;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        componentModel = "spring"
)
public interface ITransactionServiceMapper {

    TransactionDTO getTransactionResponseFromResultAndValue(TransactionResult transactionResult, Integer value);

    TransactionDTO getTransactionResponseFromResult(TransactionResult transactionResult);
}
