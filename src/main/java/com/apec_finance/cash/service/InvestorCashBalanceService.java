package com.apec_finance.cash.service;

import com.apec_finance.cash.model.InvestorCashBalance;
import com.apec_finance.cash.model.UpdateCashBalance;
import com.apec_finance.cash.model.CreateCashBalance;
import java.math.BigDecimal;


public interface InvestorCashBalanceService {
    InvestorCashBalance getCashBalance(Long investorId);

    BigDecimal getBalance(Long investorId);
    void updateCashBalance(UpdateCashBalance updateCashBalance);
    void createCashBalance(CreateCashBalance investorId);
}
