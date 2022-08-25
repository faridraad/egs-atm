package com.egs.atm;

import com.egs.atm.configuration.resources.ApplicationProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionServiceTest {

    private final ApplicationProperties applicationProperties = mock(ApplicationProperties.class);
    @Autowired
    private MockMvc mockMvc;
    final String baseUrl = applicationProperties.getProperty("bank.transaction.base.url");
    final String balance = applicationProperties.getProperty("bank.transaction.balance.url");

    @BeforeAll
    static void init() {

    }

    @Test
    void userBalance() throws Exception {
        assertNotNull(mockMvc.perform(get(baseUrl + balance)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()));

    }
}
