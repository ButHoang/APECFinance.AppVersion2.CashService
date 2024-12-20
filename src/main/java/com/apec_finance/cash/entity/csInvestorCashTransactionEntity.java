package com.apec_finance.cash.entity;

import com.apec_finance.cash.comon.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "cs_investor_cash_transaction")
public class csInvestorCashTransactionEntity extends BaseEntity {
    private long investorId;
    private String transactionType;
    private BigDecimal tran_amount;
    
}
