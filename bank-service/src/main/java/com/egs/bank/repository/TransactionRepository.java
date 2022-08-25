package com.egs.bank.repository;

import com.egs.bank.model.entity.Transaction;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    @Aggregation(pipeline = {
            "{$match:{'userId' : ?0}}",
            "{$group :{'_id': null, 'total' : { $sum: '$value' } }}"
    })
    Long sumValues(String userId);

    Optional<Transaction> findByTransactionId(String transactionId);

    Optional<Transaction> findByRolledBackFor(String rolledBackFor);
}
