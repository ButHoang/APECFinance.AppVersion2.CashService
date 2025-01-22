package com.apec_finance.cash.service;

import com.apec_finance.cash.model.CreateCashTransaction;
import com.apec_finance.cash.model.VerifyCashTransaction;

public interface InvestorCashTransactionService {
    void createCashTransaction(CreateCashTransaction createCashTransaction);
    void verifyDepositStockCashTransaction(VerifyCashTransaction verifyCashTransaction);
    void verifyWithDrawStockCashTransaction(VerifyCashTransaction verifyCashTransaction);
}
