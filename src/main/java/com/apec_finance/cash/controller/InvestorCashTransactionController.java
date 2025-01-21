package com.apec_finance.cash.controller;

import com.apec_finance.cash.comon.ResponseBuilder;
import com.apec_finance.cash.model.CreateCashTransaction;
import com.apec_finance.cash.model.UpdateCashBalance;
import com.apec_finance.cash.model.VerifyCashTransaction;
import com.apec_finance.cash.service.InvestorCashBalanceService;
import com.apec_finance.cash.service.InvestorCashTransactionService;
import com.apec_finance.cash.model.CashTransactionHistory;
import com.apec_finance.cash.model.CashTransactionHistoryRes;

import lombok.RequiredArgsConstructor;

import java.util.List;

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

    @PostMapping("/deposit")
    public ResponseBuilder<Void> createDepositCashTransaction(@RequestBody CreateCashTransaction createCashTransaction) {
        investorCashTransactionService.createDepositCashTransaction(createCashTransaction);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }

    @PostMapping("/history")
    public ResponseBuilder<List<CashTransactionHistoryRes>> historyCashTransaction(@RequestBody CashTransactionHistory CashTransactionHistory) {
        // System.out.println(CashTransactionHistory);
        List<CashTransactionHistoryRes> res =  investorCashTransactionService.historyCashTransaction(CashTransactionHistory);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", res);
    }

    @PostMapping("/verify")
    public ResponseBuilder<Void> verifyCashTransaction(@RequestBody VerifyCashTransaction verifyCashTransaction) {
        investorCashTransactionService.verifyCashTransaction(verifyCashTransaction);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }
}
