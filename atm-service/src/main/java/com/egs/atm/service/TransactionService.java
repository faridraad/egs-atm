package com.egs.atm.service;

import com.egs.atm.configuration.exception.FeignCustomException;
import com.egs.atm.configuration.resources.ApplicationProperties;
import com.egs.atm.intrface.ITransaction;
import com.egs.atm.feign.ITransactionRestApi;
import com.egs.atm.model.domain.TransactionRequest;
import com.egs.atm.utility.ConstantParam;
import com.egs.atm.utility.ExceptionConverter;
import com.egs.atm.model.dto.BalanceDTO;
import com.egs.atm.model.dto.PredefinedValueDTO;
import com.egs.atm.model.dto.TransactionDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionService implements ITransaction {

    private final ITransactionRestApi iTransactionRestApi;
    private final ApplicationProperties applicationProperties;

    @CircuitBreaker(name = "bank-transaction", fallbackMethod = "getPredefinedValuesFallBack")
    @Override
    public List<PredefinedValueDTO> getPredefinedValues(String token) {
        return iTransactionRestApi.getPredefinedValues(bearer(token));
    }

    @CircuitBreaker(name = "bank-transaction", fallbackMethod = "getBalanceFallBack")
    @Override
    public BalanceDTO getBalance(String token) {

        return iTransactionRestApi.getBalance(bearer(token));
    }

    @CircuitBreaker(name = "bank-transaction", fallbackMethod = "withdrawFallBack")
    @Override
    public TransactionDTO withdraw(String token, TransactionRequest transactionRequest) {
        return iTransactionRestApi.withdraw(transactionRequest, bearer(token));
    }
    // Used at CircuitBreaker
    private TransactionDTO withdrawFallBack( Throwable throwable){
        throw getProperExceptionFromCoreBankException(throwable);
    }

    @CircuitBreaker(name = "bank-transaction", fallbackMethod = "depositFallBack")
    @Override
    public TransactionDTO deposit(String token, TransactionRequest transactionRequest) {
        return iTransactionRestApi.deposit(transactionRequest, bearer(token));
    }

    // Used at CircuitBreaker
    private TransactionDTO depositFallBack( Throwable throwable){
        throw getProperExceptionFromCoreBankException(throwable);
    }

    @CircuitBreaker(name = "bank-transaction", fallbackMethod = "rollbackFallBack")
    @Override
    public TransactionDTO rollback(String token, String transactionId) {
        return iTransactionRestApi.rollback(transactionId, bearer(token));
    }
    private TransactionDTO rollbackFallBack(Throwable throwable){
        throw getProperExceptionFromCoreBankException(throwable);
    }



    private List<PredefinedValueDTO> getPredefinedValuesFallBack( Throwable e) {
        throw getProperExceptionFromCoreBankException(e);
    }



    // used at CircuitBreaker
    private BalanceDTO getBalanceFallBack( Throwable throwable){
        throw getProperExceptionFromCoreBankException(throwable);
    }


    private String bearer(String token) {
        return ConstantParam.Bearer + token;
    }

    private RuntimeException getProperExceptionFromCoreBankException(Throwable throwable) {
        if (throwable.getClass().equals(FeignCustomException.class)){
            return ExceptionConverter.getProperExceptionFromFeignGeneralException((FeignCustomException) throwable);
        }else{
            // circuit opened
            return new FeignCustomException.ServiceUnavailable(applicationProperties.getProperty("error.feign.exception.text"));
        }
    }
}
