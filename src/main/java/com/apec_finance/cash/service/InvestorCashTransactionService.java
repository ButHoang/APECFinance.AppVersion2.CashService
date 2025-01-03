package com.apec_finance.cash.service;

import com.apec_finance.cash.model.CreateCashTransaction;

public interface InvestorCashTransactionService {
    void createCashTransaction(CreateCashTransaction createCashTransaction);
    void createWithdrawalTransaction(CreateCashTransaction createCashTransaction);
    void verifyCashTransaction(Long transactionId);
}
