package com.egs.atm.controller;

import com.egs.atm.intrface.ITransaction;
import com.egs.atm.model.dto.BalanceDTO;
import com.egs.atm.model.dto.PredefinedValueDTO;
import com.egs.atm.model.domain.TransactionRequest;
import com.egs.atm.model.dto.TransactionDTO;
import com.egs.atm.utility.ConstantParam;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "transaction api list")
@RestController
@RequestMapping("v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final ITransaction iTransaction;
    private final HttpServletRequest request;

    @GetMapping("/withdraw/predefines")
    public List<PredefinedValueDTO> getPredefinedValues(){
        return iTransaction.getPredefinedValues(request.getHeader(ConstantParam.Authorization));
    }

    @SneakyThrows
    @GetMapping("/balance")
    public BalanceDTO getBalance(){
        return iTransaction.getBalance(request.getHeader(ConstantParam.Authorization));
    }

    @PostMapping("/withdraw")
    public TransactionDTO withdraw(@RequestBody TransactionRequest transactionRequest){
        return iTransaction.withdraw(request.getHeader(ConstantParam.Authorization), transactionRequest);
    }

    @PostMapping("/deposit")
    public TransactionDTO deposit(@RequestBody TransactionRequest transactionRequest){
        return iTransaction.deposit(request.getHeader(ConstantParam.Authorization), transactionRequest);
    }

    @PostMapping("/rollback/{txId}")
    public TransactionDTO rollback(@PathVariable("txId") String transactionId){
        return iTransaction.rollback(request.getHeader(ConstantParam.Authorization), transactionId);
    }
}
