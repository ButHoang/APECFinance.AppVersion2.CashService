package com.apec_finance.cash.entity;

import com.apec_finance.cash.comon.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cs_investor_cash_balance")
public class CsInvestorCashBalanceEntity extends BaseEntity {

    @Column(name = "investor_id", nullable = false)
    private Long investorId;

    @Column(nullable = false, length = 50)
    private String investorAccountNo;

    @Column(nullable = false)
    private float balance = 0;

    @Column(nullable = false)
    private float holdBalance = 0;

    @Column(nullable = false)
    private float pendBalance = 0;

    @Column(nullable = false, length = 10)
    private String currency = "VND";

    @Column(nullable = false, length = 1)
    private String status;

}

