package com.apec_finance.cash.controller;

import com.apec_finance.cash.comon.ResponseBuilder;
import com.apec_finance.cash.model.CreateCashTransaction;
import com.apec_finance.cash.model.UpdateCashBalance;
import com.apec_finance.cash.service.InvestorCashBalanceService;
import com.apec_finance.cash.service.InvestorCashTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/investor-cash-transaction")
@RequiredArgsConstructor
public class InvestorCashTransactionController {
    private final InvestorCashTransactionService investorCashTransactionService;

    @PostMapping("/create")
    public ResponseBuilder<Void> createCashTransaction(@RequestBody CreateCashTransaction createCashTransaction) {
        investorCashTransactionService.createCashTransaction(createCashTransaction);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }

    @PostMapping("/withdrawal")
    public ResponseBuilder<Void> createWithdrawalTransaction(@RequestBody CreateCashTransaction createCashTransaction) {
        investorCashTransactionService.createWithdrawalTransaction(createCashTransaction);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }
}
