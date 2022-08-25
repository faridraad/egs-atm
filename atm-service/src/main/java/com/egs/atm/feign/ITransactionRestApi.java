package com.egs.atm.feign;

import com.egs.atm.model.dto.BalanceDTO;
import com.egs.atm.model.dto.PredefinedValueDTO;
import com.egs.atm.model.domain.TransactionRequest;
import com.egs.atm.model.dto.TransactionDTO;
import com.egs.atm.utility.ConstantParam;
import com.egs.atm.utility.DefaultFeignErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "bank-transaction-client", url = "${bank.transaction.base.url}", configuration = DefaultFeignErrorDecoder.class)
public interface ITransactionRestApi {

    @PostMapping("${bank.transaction.withdraw.url}")
    TransactionDTO withdraw(@RequestBody TransactionRequest transactionRequest,
                              @RequestHeader(ConstantParam.Authorization) String authHeader);

    @PostMapping("${bank.transaction.deposit.url}")
    TransactionDTO deposit(@RequestBody TransactionRequest transactionRequest,
                             @RequestHeader(ConstantParam.Authorization) String authHeader);

    @PostMapping("${bank.transaction.rollback.url}")
    TransactionDTO rollback(@PathVariable("transactionId") String transactionId,
                              @RequestHeader(ConstantParam.Authorization) String authHeader);

    @GetMapping("${bank.transaction.predefined.url}")
    List<PredefinedValueDTO> getPredefinedValues(@RequestHeader(ConstantParam.Authorization) String authHeader);

    @GetMapping("${bank.transaction.balance.url}")
    BalanceDTO getBalance(@RequestHeader(ConstantParam.Authorization) String authHeader);
}
