package com.apec_finance.cash.service.impl;

import com.apec_finance.cash.entity.CsInvestorCashBalanceEntity;
import com.apec_finance.cash.entity.InvestorCashTransactionEntity;
import com.apec_finance.cash.mapper.InvestorCashTransactionMapper;
import com.apec_finance.cash.model.CreateCashTransaction;

import com.apec_finance.cash.repository.InvestorCashBalanceRepository;
import com.apec_finance.cash.repository.InvestorCashTransactionRepository;
import com.apec_finance.cash.model.VerifyCashTransaction;
import com.apec_finance.cash.service.InvestorCashTransactionService;
import com.apec_finance.cash.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class InvestorCashTransactionImpl implements InvestorCashTransactionService {
    private final InvestorCashTransactionRepository investorCashTransactionRepository;
    private final InvestorCashBalanceRepository investorCashBalanceRepository;
    private final InvestorCashTransactionMapper investorCashTransactionMapper;
    private final KeycloakService keycloakService;

    @Override
    public void createCashTransaction(CreateCashTransaction createCashTransaction) {
        InvestorCashTransactionEntity investorCashTransactionEntity = investorCashTransactionMapper.toEntity(createCashTransaction);
        investorCashTransactionEntity.setTranNo(generateTransactionNumber(createCashTransaction.getTranType(), createCashTransaction.getTranDate()));
        investorCashTransactionRepository.save(investorCashTransactionEntity);
    }

    @Transactional
    public void verifyDepositStockCashTransaction(VerifyCashTransaction verifyCashTransaction) {
        InvestorCashTransactionEntity cashTransaction = investorCashTransactionRepository
                .findByRefIdAndTranTypeAndOprAndDeleted(verifyCashTransaction.getRefId(), "ORD", "-", 0);
        cashTransaction.setStatus("A");
        cashTransaction.setVerifiedDate(OffsetDateTime.now());
        cashTransaction.setVerifiedBy(keycloakService.getNameFromToken());

        CsInvestorCashBalanceEntity investorCashBalanceEntity = investorCashBalanceRepository.findByInvestorId(keycloakService.getInvestorIdFromToken());
        investorCashBalanceEntity.setHoldBalance((float) (investorCashBalanceEntity.getHoldBalance() - cashTransaction.getTranAmount()));

        investorCashTransactionRepository.save(cashTransaction);
        investorCashBalanceRepository.save(investorCashBalanceEntity);
    }

    @Transactional
    public void verifyWithDrawStockCashTransaction(VerifyCashTransaction verifyCashTransaction) {
        InvestorCashTransactionEntity cashTransaction = investorCashTransactionRepository
                .findByRefIdAndTranTypeAndOprAndDeleted(verifyCashTransaction.getRefId(), "REV", "+", 0);
        cashTransaction.setStatus("A");
        cashTransaction.setVerifiedDate(OffsetDateTime.now());
        cashTransaction.setVerifiedBy(keycloakService.getNameFromToken());

        CsInvestorCashBalanceEntity investorCashBalanceEntity = investorCashBalanceRepository.findByInvestorId(keycloakService.getInvestorIdFromToken());
        investorCashBalanceEntity.setBalance((float) (investorCashBalanceEntity.getBalance() + cashTransaction.getTranAmount()));

        investorCashTransactionRepository.save(cashTransaction);
        investorCashBalanceRepository.save(investorCashBalanceEntity);
    }


    public static String generateTranNo(String tranType, LocalDate tranDate, int transactionCount) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = tranDate.format(dateFormatter);

        String transactionNumber = String.format("%06d", transactionCount);
        return tranType + formattedDate + "." + transactionNumber;
    }

    public String generateTransactionNumber(String tranType, LocalDate tranDate) {
        int transactionCount = investorCashTransactionRepository.countTransactionsByDateAndType(tranDate, tranType);

        transactionCount += 1;
        return generateTranNo(tranType, tranDate, transactionCount);
    }
}