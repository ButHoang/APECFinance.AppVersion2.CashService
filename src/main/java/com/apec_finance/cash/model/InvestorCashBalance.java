package com.apec_finance.cash.model;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class InvestorCashBalance {
    BigDecimal totalBalance;

//    public void setTotalBalance(float totalBalance) {
//        this.totalBalance = new BigDecimal(totalBalance).setScale(2, RoundingMode.HALF_UP).floatValue();
//    }
}
