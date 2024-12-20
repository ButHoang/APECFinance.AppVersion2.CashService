package com.apec_finance.cash.controller;

import com.apec_finance.cash.comon.ResponseBuilder;
import com.apec_finance.cash.model.InvestorCashTransaction;
import com.apec_finance.cash.service.InvestorCashBalanceService;
import com.apec_finance.cash.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/investor-cash-balance")
@RequiredArgsConstructor
public class InvestorCashTransactionController {
    private final InvestorCashTransactionService InvestorCashTransactionService;


}
