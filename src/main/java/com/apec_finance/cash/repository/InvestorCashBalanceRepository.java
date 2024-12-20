package com.apec_finance.cash.repository;

import com.apec_finance.cash.entity.CsInvestorCashBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface InvestorCashBalanceRepository extends JpaRepository<CsInvestorCashBalanceEntity, Long> {
    CsInvestorCashBalanceEntity findByInvestorId(Long investorId);
    @Query("SELECT c FROM CsInvestorCashBalanceEntity c WHERE c.investorId = :investorId AND c.status = :status and c.deleted = 0")
    CsInvestorCashBalanceEntity findByInvestorIdAndStatus(@Param("investorId") Long investorId, @Param("status") String status);
}


