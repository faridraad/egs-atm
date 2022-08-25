package com.egs.bank.controller;

import com.egs.bank.model.dto.BalanceDTO;
import com.egs.bank.model.dto.PredefinedValueDTO;
import com.egs.bank.model.domain.TransactionRequest;
import com.egs.bank.model.dto.TransactionDTO;
import com.egs.bank.service.TransactionService;
import com.egs.bank.utility.ConstantParam;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("v1/transaction")
@Api(tags = "transaction api list")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final HttpServletRequest request;

    @PostMapping("/withdraw")
    public TransactionDTO withdraw(@RequestBody TransactionRequest transactionRequest){
        return transactionService.withdraw(transactionRequest, request.getHeader(ConstantParam.Authorization));
    }

    @PostMapping("/deposit")
    public TransactionDTO deposit(@RequestBody TransactionRequest transactionRequest){
        return transactionService.deposit(transactionRequest, request.getHeader(ConstantParam.Authorization));
    }

    @PostMapping("/rollback/{txId}")
    public TransactionDTO rollback(@PathVariable("txId") String txId){
        return transactionService.rollbackTransaction(txId);
    }

    @GetMapping("/withdraw/predefined")
    public List<PredefinedValueDTO> getPredefinedValues(){
        return transactionService.getPredefinedValueList();
    }

    @GetMapping("/balance")
    public BalanceDTO getBalance()  {
        return transactionService.getBalance(request.getHeader(ConstantParam.Authorization));
    }
}
