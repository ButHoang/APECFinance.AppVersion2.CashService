package com.apec_finance.cash.service;

import com.apec_finance.cash.model.CreateCashTransaction;

import java.util.List;

import com.apec_finance.cash.model.CashTransactionHistory;
import com.apec_finance.cash.model.CashTransactionHistoryPaging;
import com.apec_finance.cash.model.CashTransactionHistoryRes;
import com.apec_finance.cash.model.VerifyCashTransaction;

public interface InvestorCashTransactionService {
    void createCashTransaction(CreateCashTransaction createCashTransaction);
    void createWithdrawalTransaction(CreateCashTransaction createCashTransaction);
    void createDepositCashTransaction(CreateCashTransaction createCashTransaction);
    CashTransactionHistoryPaging historyCashTransaction(CashTransactionHistory cashTransactionHistory);
    void verifyDepositStockCashTransaction(VerifyCashTransaction verifyCashTransaction);
    void verifyWithDrawStockCashTransaction(VerifyCashTransaction verifyCashTransaction);
}
