package com.apec_finance.cash.model;

import lombok.Data;

@Data
public class UpdateCashBalance {
    private Long investorId;
    private Long balance;
    private Long holdBalance;
}
