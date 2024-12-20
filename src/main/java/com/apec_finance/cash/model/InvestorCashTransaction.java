package com.apec_finance.cash.model;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data

public class InvestorCashTransaction {
    private long investorId;
    private String transactionType;
    private BigDecimal amount;
    
}



