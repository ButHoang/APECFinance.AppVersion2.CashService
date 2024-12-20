package com.apec_finance.cash.repository;

import com.apec_finance.cash.entity.CsInvestorCashTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
@Repository
public interface InvestorCashTransactionRepository extends JpaRepository<CsInvestorCashTransactionEntity, Long> {

    @Modifying
    @Transactional
    @Query(
        value = "INSERT INTO cs_investor_cash_transaction (investor_id, tran_date, tran_time, tran_no, tran_type, opr, tran_amount, fee_amount, final_amount, status) " +
                "VALUES (:investorId, CURRENT_DATE, CURRENT_TIME, :tranNo, :tranType, :opr, :tranAmount, 0, :tranAmount, :status)",
        nativeQuery = true
    )
    void addCashTransactionEntity(
        @Param("investorId") Long investorId,
        @Param("tranNo") String tranNo,
        @Param("tranType") String tranType,
        @Param("opr") String opr,
        @Param("tranAmount") BigDecimal tranAmount,
        @Param("status") String status
    );
}