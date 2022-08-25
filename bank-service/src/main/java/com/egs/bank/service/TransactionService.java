package com.egs.bank.service;

import com.egs.bank.configuration.exception.ConflictException;
import com.egs.bank.configuration.exception.NotFoundException;
import com.egs.bank.configuration.exception.PreconditionFailedException;
import com.egs.bank.configuration.resources.ApplicationProperties;
import com.egs.bank.intrface.ITransaction;
import com.egs.bank.model.domain.TransactionRequest;
import com.egs.bank.model.dto.BalanceDTO;
import com.egs.bank.model.dto.PredefinedValueDTO;
import com.egs.bank.model.dto.TransactionDTO;
import com.egs.bank.model.mapper.ITransactionServiceMapper;
import com.egs.bank.model.mapper.TransactionMapper;
import com.egs.bank.model.domain.TransactionResult;
import com.egs.bank.model.entity.Transaction;
import com.egs.bank.repository.TransactionRepository;
import com.egs.bank.utility.AuthUtil;
import com.egs.bank.utility.JwtTokenGranter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransaction {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper mapper;
    private final ITransactionServiceMapper iTransactionServiceMapper;
    private final JwtTokenGranter tokenGranter;
    private final ApplicationProperties applicationProperties;

    @Override
    public List<Integer> getPredefinedValues() {
        return List.of(10000,20000,30000,50000,100000);
    }

    @Override
    public TransactionResult withdraw(Integer value, String userId) {
        long balance = getBalanceByCheckingWithdrawCondition(userId, value);
        Transaction transaction = mapper.getTransaction(userId, value * -1, UUID.randomUUID().toString(),"withdraw");
        transactionRepository.save(transaction);
        return mapper.getTransactionResult(transaction, balance - value);
    }

    @Override
    public TransactionResult deposit(Integer value, String userId) {
        long balance = getUserBalanceByNullConsideration(userId);
        Transaction transaction = mapper.getTransaction(userId, value, UUID.randomUUID().toString(),"deposit");
        transactionRepository.save(transaction);
        return mapper.getTransactionResult(transaction, balance + value);
    }

    @Override
    public TransactionResult rollback(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId)
                .map(this::doRollbackTransaction)
                .orElseThrow(() -> {throw new NotFoundException(applicationProperties.getProperty("error.transaction.not.found.exception.text"));});
    }

    @Override
    public long getUserBalance(String userId) {
        return getUserBalanceByNullConsideration(userId);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public TransactionDTO withdraw(TransactionRequest transactionRequest, String authorizationHeader) {
        String userId = tokenGranter.getUserIdFromToken(AuthUtil.getBearerToken(authorizationHeader));
        TransactionResult transactionResult = this.withdraw(transactionRequest.getValue(), userId);
        return iTransactionServiceMapper.getTransactionResponseFromResultAndValue(transactionResult, transactionRequest.getValue());
    }

    public TransactionDTO deposit(TransactionRequest transactionRequest, String authorizationHeader) {
        String userId = tokenGranter.getUserIdFromToken(AuthUtil.getBearerToken(authorizationHeader));
        TransactionResult transactionResult = this.deposit(transactionRequest.getValue(), userId);
        return iTransactionServiceMapper.getTransactionResponseFromResultAndValue(transactionResult, transactionRequest.getValue());
    }
    public List<PredefinedValueDTO> getPredefinedValueList() {
        return this.getPredefinedValues().stream()
                .map(PredefinedValueDTO::new)
                .collect(Collectors.toList());
    }

    public BalanceDTO getBalance(String authorizationHeader) {
        String userId = tokenGranter.getUserIdFromToken(AuthUtil.getBearerToken(authorizationHeader));
        long balance = this.getUserBalance(userId);
        return new BalanceDTO(balance);
    }

    public TransactionDTO rollbackTransaction(String transactionId) {
        TransactionResult transactionResult = this.rollback(transactionId);
        return iTransactionServiceMapper.getTransactionResponseFromResult(transactionResult);
    }

    private long getBalanceByCheckingWithdrawCondition(String userId, Integer value) {
        long balance = getUserBalanceByNullConsideration(userId);
        if (balance < value)
            throw new PreconditionFailedException(applicationProperties.getProperty("error.balance.exception.text") + balance);
        return balance;
    }

    private long getUserBalanceByNullConsideration(String userId) {
        Long result = transactionRepository.sumValues(userId);
        return Objects.isNull(result) ? 0 : result;
    }

    private TransactionResult doRollbackTransaction(Transaction transaction) {
        checkForRolledBackBefore(transaction.getTransactionId());
        long balance = getUserBalanceByNullConsideration(transaction.getUserId());
        Transaction rollbackTransaction = mapper.getRollbackTransactionFromTransaction(
                transaction, UUID.randomUUID().toString());
        rollbackTransaction = transactionRepository.save(rollbackTransaction);
        return mapper.getTransactionResult(rollbackTransaction, balance + rollbackTransaction.getValue());
    }

    private void checkForRolledBackBefore(String transactionId) {
        transactionRepository.findByRolledBackFor(transactionId).ifPresent(transaction -> {
            throw new ConflictException(applicationProperties.getProperty("error.duplicate.rollback.exception.text"));
        });
    }
}
