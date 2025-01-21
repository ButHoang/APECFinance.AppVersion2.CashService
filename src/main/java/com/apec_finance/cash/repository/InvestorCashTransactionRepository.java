package com.apec_finance.cash.repository;

import com.apec_finance.cash.entity.InvestorCashTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import java.time.LocalDate;

public interface InvestorCashTransactionRepository extends JpaRepository<InvestorCashTransactionEntity, Long> {
    @Query("SELECT COUNT(t) FROM InvestorCashTransactionEntity t WHERE t.tranDate = :tranDate AND t.tranType = :tranType")
    int countTransactionsByDateAndType(@Param("tranDate") LocalDate tranDate , @Param("tranType") String tranType);
    @Query("SELECT t FROM InvestorCashTransactionEntity t WHERE t.tranDate >= :startDate AND t.tranDate <= :endDate  AND t.investorId = :investorId ORDER BY t.createdDate ASC")
    List<InvestorCashTransactionEntity> findTransactionsByDateTypeAndInvestor(
        @Param("investorId") Long investorId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    InvestorCashTransactionEntity findByRefIdAndTranTypeAndOprAndDeleted(Long refId, String tranType, String opr, Integer deleted);
}
