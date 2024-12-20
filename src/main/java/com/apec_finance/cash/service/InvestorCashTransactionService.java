package com.apec_finance.cash.service;

import java.math.BigDecimal;

import com.apec_finance.cash.model.InvestorCashTransaction;

public interface InvestorCashTransactionService {
    String addCashTransaction(Long investorId , BigDecimal tranAmount);
}
