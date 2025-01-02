package com.apec_finance.cash.controller;

import com.apec_finance.cash.comon.ResponseBuilder;
import com.apec_finance.cash.model.InvestorCashBalance;
import com.apec_finance.cash.model.UpdateCashBalance;
import com.apec_finance.cash.service.InvestorCashBalanceService;
import com.apec_finance.cash.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.apec_finance.cash.model.CreateCashBalance;

import java.math.BigDecimal;

@RestController
@RequestMapping("/investor-cash-balance")
@RequiredArgsConstructor
public class InvestorCashBalanceController {
    private final InvestorCashBalanceService investorCashBalanceService;

    @GetMapping("/get")
    public ResponseBuilder<InvestorCashBalance> getCashBalance(KeycloakService keycloakService) {
        InvestorCashBalance rs = investorCashBalanceService.getCashBalance(keycloakService.getInvestorIdFromToken());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping("/balance")
    public ResponseBuilder<BigDecimal> getBalance(KeycloakService keycloakService) {
        var rs = investorCashBalanceService.getBalance(keycloakService.getInvestorIdFromToken());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
    @PostMapping("/update")
    public ResponseBuilder<Void> updateBalance(@RequestBody UpdateCashBalance updateCashBalance) {
        investorCashBalanceService.updateCashBalance(updateCashBalance);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }
    @PostMapping("/create")
    public ResponseBuilder<Void> createBalance(@RequestBody CreateCashBalance investorId) {
        System.out.println(investorId);
        investorCashBalanceService.createCashBalance(investorId);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", null);
    }
}
