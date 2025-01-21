package com.apec_finance.cash.service;

import com.apec_finance.cash.model.CreateCashTransaction;

import java.util.List;

import com.apec_finance.cash.model.CashTransactionHistory;
import com.apec_finance.cash.model.CashTransactionHistoryRes;

public interface InvestorCashTransactionService {
    void createCashTransaction(CreateCashTransaction createCashTransaction);
    void createWithdrawalTransaction(CreateCashTransaction createCashTransaction);
    void verifyCashTransaction(Long transactionId);
    void createDepositCashTransaction(CreateCashTransaction createCashTransaction);
    List<CashTransactionHistoryRes> historyCashTransaction(CashTransactionHistory cashTransactionHistory);
}
