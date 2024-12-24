package com.apec_finance.cash.repository;

import com.apec_finance.cash.entity.InvestorCashTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestorCashTransactionRepository extends JpaRepository<InvestorCashTransactionEntity, Long> {
}
