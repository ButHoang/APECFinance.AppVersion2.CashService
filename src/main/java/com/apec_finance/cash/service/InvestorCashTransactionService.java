package com.apec_finance.cash.service;

import com.apec_finance.cash.model.CreateCashTransaction;
import com.apec_finance.cash.model.CashTransactionHistory;

public interface InvestorCashTransactionService {
    void createCashTransaction(CreateCashTransaction createCashTransaction);
    void createWithdrawalTransaction(CreateCashTransaction createCashTransaction);
    void verifyCashTransaction(Long transactionId);
    void createDepositCashTransaction(CreateCashTransaction createCashTransaction);
    void historyCashTransaction(CashTransactionHistory cashTransactionHistory);
}
