package com.apec_finance.cash.model;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;


@Data

public class InvestorCashTransaction {
    private long investorId;
    private LocalDate tranDate;
    private String tranTime;
    private String tranNo;
    private String tranType;
    private String opr;
    private BigDecimal tranAmount;
    private BigDecimal feeAmount;
    private BigDecimal finalAmount;
    private String status;

}



