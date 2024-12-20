package com.apec_finance.cash.entity;

import com.apec_finance.cash.comon.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "cs_investor_cash_transaction")
public class CsInvestorCashTransactionEntity extends BaseEntity {
    
    @Column(name = "investor_id", nullable = false)
    private Long investorId;

    @Column(name = "tran_date", nullable = false)
    private LocalDate tranDate;

    @Column(name = "tran_time", nullable = false)
    private String tranTime;

    @Column(name = "tran_no", nullable = false)
    private String tranNo;

    @Column(name = "tran_type", nullable = false)
    private String tranType;

    @Column(name = "opr", nullable = false)
    private String opr;

    @Column(name = "tran_amount", nullable = false)
    private BigDecimal tranAmount;

    @Column(name = "fee_amount", nullable = false)
    private BigDecimal feeAmount;

    @Column(name = "final_amount", nullable = false)
    private BigDecimal finalAmount;

    @Column(name = "status", nullable = false)
    private String status;

}
