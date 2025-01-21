package com.apec_finance.cash.repository;

import com.apec_finance.cash.entity.CsInvestorCashBalanceHistoryEntity;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


public interface InvestorCashBalanceHistoryRepository extends JpaRepository<CsInvestorCashBalanceHistoryEntity, Long> {
    @Query("SELECT c FROM CsInvestorCashBalanceHistoryEntity c WHERE c.investorId = :investorId AND c.tradingDate = :tradingDate  and c.status = 'A' and c.deleted = 0")
    CsInvestorCashBalanceHistoryEntity findByInvestorIdAndTradingDate(@Param("investorId") Long investorId ,@Param("tradingDate") LocalDate tradingDate );
    @Query("SELECT c FROM CsInvestorCashBalanceHistoryEntity c WHERE c.investorId = :investorId AND c.status = :status and c.deleted = 0")
    CsInvestorCashBalanceHistoryEntity findByInvestorIdAndStatus(@Param("investorId") Long investorId, @Param("status") String status);

}


// AND c.tradingDate = :tradingDate
// , @Param("tradingDate") LocalDate tradingDate