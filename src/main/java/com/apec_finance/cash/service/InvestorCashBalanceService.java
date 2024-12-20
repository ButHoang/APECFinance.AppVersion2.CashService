package com.apec_finance.cash.service;

import com.apec_finance.cash.model.InvestorCashBalance;

public interface InvestorCashBalanceService {
    InvestorCashBalance getCashBalance(Long investorId);
}
