package com.egs.bank;

import com.egs.bank.configuration.resources.ApplicationProperties;
import com.egs.bank.model.entity.Transaction;
import com.egs.bank.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class TransactionServiceTest {
    private final TransactionRepository transactionRepository = mock(TransactionRepository.class);

    @Test
    void findByTransactionId() {
        Optional<Transaction> result = transactionRepository.findByTransactionId(UUID.randomUUID().toString());
        assertNotNull(result);
    }
}
