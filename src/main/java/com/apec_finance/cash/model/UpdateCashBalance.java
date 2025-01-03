package com.apec_finance.cash.model;

import lombok.Data;

@Data
public class UpdateCashBalance {
    private Long investorId;
    private Float paidAmount;
    private String createdBy;
    private String type;
}
