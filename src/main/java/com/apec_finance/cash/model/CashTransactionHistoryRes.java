package com.apec_finance.cash.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
@Data
public class CashTransactionHistoryRes {
    String tranType;
    String opr;
    BigDecimal tranAmount;
    String description;
    String status;
    BigDecimal balanceAfTrans;
    Integer productId;
    String createdDate;
}
