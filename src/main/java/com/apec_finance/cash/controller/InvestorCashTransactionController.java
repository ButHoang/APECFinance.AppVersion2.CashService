package com.apec_finance.cash.controller;

import com.apec_finance.cash.comon.ResponseBuilder;
import com.apec_finance.cash.model.InvestorCashTransaction;
import com.apec_finance.cash.service.InvestorCashTransactionService;
import com.apec_finance.cash.service.KeycloakService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.checkerframework.checker.units.qual.s;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apec_finance.cash.mapper.AddCashTransactionRequest;

@RestController
@RequestMapping("/investor-cash-transaction")
@RequiredArgsConstructor
public class InvestorCashTransactionController {
    private final InvestorCashTransactionService InvestorCashTransactionService;

    @PostMapping("/add")
    public ResponseBuilder<String> addCashTransaction(@RequestBody AddCashTransactionRequest request ,  KeycloakService keycloakService) {
        BigDecimal tranAmount = request.getTranAmount();
        System.out.println("tranAmount is : " + tranAmount);
        String rs = InvestorCashTransactionService.addCashTransaction(keycloakService.getInvestorIdFromToken(), tranAmount);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", "success"  );
    }
}
