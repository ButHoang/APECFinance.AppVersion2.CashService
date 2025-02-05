package com.apec_finance.cash.service.impl;

import com.apec_finance.cash.entity.CsInvestorCashBalanceEntity;
import com.apec_finance.cash.entity.InvestorCashTransactionEntity;
import com.apec_finance.cash.mapper.InvestorCashTransactionMapper;
import com.apec_finance.cash.model.CreateCashTransaction;
import com.apec_finance.cash.repository.InvestorCashBalanceRepository;
import com.apec_finance.cash.repository.InvestorCashTransactionRepository;
import com.apec_finance.cash.service.AppClient;
import com.apec_finance.cash.model.VerifyCashTransaction;
import com.apec_finance.cash.repository.InvestorCashBalanceRepository;
import com.apec_finance.cash.repository.InvestorCashTransactionRepository;
import com.apec_finance.cash.model.VerifyCashTransaction;
import com.apec_finance.cash.service.InvestorCashTransactionService;
import com.apec_finance.cash.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.apec_finance.cash.service.KeycloakService;
import com.apec_finance.cash.service.TradingClient;
import com.apec_finance.cash.service.InvestorCashBalanceService;
import com.apec_finance.cash.comon.ResponseBuilder;
import com.apec_finance.cash.entity.CsInvestorCashBalanceEntity;
import com.apec_finance.cash.entity.CsInvestorCashBalanceHistoryEntity;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import com.apec_finance.cash.model.CashTransactionHistoryRes;


import com.apec_finance.cash.model.RsInvestorBankAcc;
import com.apec_finance.cash.model.RsTransactionRange;
import com.apec_finance.cash.model.TransactionRange;
import com.apec_finance.cash.repository.InvestorCashBalanceHistoryRepository;
import com.apec_finance.cash.model.CashTransactionHistory;
import com.apec_finance.cash.model.CashTransactionHistoryPaging;

@Service
@RequiredArgsConstructor
public class InvestorCashTransactionImpl implements InvestorCashTransactionService {
    private final InvestorCashTransactionRepository investorCashTransactionRepository;
    private final InvestorCashBalanceRepository investorCashBalanceRepository;
    private final InvestorCashTransactionMapper investorCashTransactionMapper;
    private final AppClient appClient;
    private final KeycloakService keycloakService;
    private final InvestorCashBalanceHistoryRepository investorCashBalanceHistoryRepository;
    private final TradingClient tradingClient;

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

    public void createWithdrawalTransaction(CreateCashTransaction createCashTransaction) {
        InvestorCashTransactionEntity investorCashTransactionEntity = investorCashTransactionMapper.toEntity(createCashTransaction);
        investorCashTransactionEntity.setTranType("OUT");
        investorCashTransactionEntity.setOpr("-");
        investorCashTransactionEntity.setStatus("P");
        investorCashTransactionEntity.setTranNo(generateTransactionNumber(createCashTransaction.getTranType(), LocalDate.now()));
        investorCashTransactionEntity.setCreatedBy("SYSTEM");
        investorCashTransactionEntity.setTranDate(LocalDate.now());
        investorCashTransactionEntity.setTranTime(LocalDateTime.now());
        RsInvestorBankAcc investorBankAccs = appClient.getInvestorBankAcc(keycloakService.getInvestorIdFromToken() ,  "*,bank_id.*");
        investorCashTransactionEntity.setBankAccount(investorBankAccs.getData().get(0).getBankAccount());
        investorCashTransactionEntity.setBankCode(investorBankAccs.getData().get(0).getBankId().getBankCode());
        try{
            CsInvestorCashBalanceEntity csInvestorCashBalanceEntity = investorCashBalanceRepository.findByInvestorIdAndStatus(keycloakService.getInvestorIdFromToken(), "A");
        float currentBalance = csInvestorCashBalanceEntity.getBalance();
        if (currentBalance < createCashTransaction.getTranAmount().floatValue()) {
            return;
        }
        float currentHoldBalance = csInvestorCashBalanceEntity.getHoldBalance();
        csInvestorCashBalanceEntity.setBalance(currentBalance - createCashTransaction.getTranAmount().floatValue());
        csInvestorCashBalanceEntity.setHoldBalance(currentHoldBalance + createCashTransaction.getTranAmount().floatValue());
        investorCashBalanceRepository.save(csInvestorCashBalanceEntity);
        }
        catch (Exception e){
            return;
        }
        
        investorCashTransactionRepository.save(investorCashTransactionEntity);
        
        RsTransactionRange transactionRangeslist = appClient.getTransactionRange();
        
        LocalTime frTime = null;
        LocalTime toTime = null;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        List<TransactionRange> transactionRanges = transactionRangeslist.getResult();
        for (TransactionRange transactionRange : transactionRanges) {
            if ("FRTIME".equals(transactionRange.getVarName())) {
                frTime = LocalTime.parse(transactionRange.getVarValue(), timeFormatter);
            } else if ("TOTIME".equals(transactionRange.getVarName())) {
                toTime = LocalTime.parse(transactionRange.getVarValue(), timeFormatter);
            }
        }
        if (frTime != null && toTime != null) {
            LocalTime currentTime = LocalTime.now();
            if (currentTime.isBefore(frTime) || currentTime.isAfter(toTime)) {
                return;
            }
            else {
                this.verifyCashTransaction(investorCashTransactionEntity.getId());
            }
        }
    }

    public void createDepositCashTransaction( CreateCashTransaction createCashTransaction) {
        // CreateCashTransaction createCashTransaction = new CreateCashTransaction();
        InvestorCashTransactionEntity investorCashTransactionEntity = investorCashTransactionMapper.toEntity(createCashTransaction);
        investorCashTransactionEntity.setTranType("IN");
        investorCashTransactionEntity.setOpr("+");
        investorCashTransactionEntity.setStatus("A");
        investorCashTransactionEntity.setTranNo(generateTransactionNumber(createCashTransaction.getTranType(), LocalDate.now()));
        investorCashTransactionEntity.setCreatedBy("SYSTEM");
        investorCashTransactionEntity.setTranDate(LocalDate.now());
        investorCashTransactionEntity.setTranTime(LocalDateTime.now());
        RsInvestorBankAcc investorBankAccs = appClient.getInvestorBankAcc(keycloakService.getInvestorIdFromToken() ,  "*,bank_id.*");
        investorCashTransactionEntity.setBankAccount(investorBankAccs.getData().get(0).getBankAccount());
        investorCashTransactionEntity.setBankCode(investorBankAccs.getData().get(0).getBankId().getBankCode());
        try{
            CsInvestorCashBalanceEntity csInvestorCashBalanceEntity = investorCashBalanceRepository.findByInvestorIdAndStatus(keycloakService.getInvestorIdFromToken(), "A");
            float currentBalance = csInvestorCashBalanceEntity.getBalance();
            csInvestorCashBalanceEntity.setBalance(currentBalance + createCashTransaction.getTranAmount().floatValue());
            investorCashBalanceRepository.save(csInvestorCashBalanceEntity);
        }
        catch (Exception e){
            return;
        }
        investorCashTransactionRepository.save(investorCashTransactionEntity);
        // RsTransactionRange transactionRangeslist = appClient.getTransactionRange();
    }
    

    public void verifyCashTransaction(Long transactionId){
        InvestorCashTransactionEntity existCashTransaction = investorCashTransactionRepository.findById(transactionId).orElse(null);
        if (existCashTransaction == null) {
            return;
        }
        existCashTransaction.setStatus("A");
        CsInvestorCashBalanceEntity investorCashBalance = investorCashBalanceRepository.findByInvestorIdAndStatus(existCashTransaction.getInvestorId(), "A");
        investorCashBalance.setHoldBalance(investorCashBalance.getHoldBalance() - existCashTransaction.getTranAmount().floatValue());
        investorCashBalanceRepository.save(investorCashBalance);
        investorCashTransactionRepository.save(existCashTransaction);
    }

    public CashTransactionHistoryPaging historyCashTransaction(CashTransactionHistory cashTransactionHistory){
        int page = cashTransactionHistory.getPage();
        int size = cashTransactionHistory.getSize();
        String dateStartString = cashTransactionHistory.getDateStart();
        LocalDate startDate = LocalDate.parse(dateStartString);
        String dateEndString = cashTransactionHistory.getDateEnd();
        LocalDate endDate = LocalDate.parse(dateEndString);
        CsInvestorCashBalanceHistoryEntity investorCashBalanceHistory = investorCashBalanceHistoryRepository.findByInvestorIdAndTradingDate( keycloakService.getInvestorIdFromToken()  , startDate );
        // , startDate
        if (investorCashBalanceHistory == null) {
            return null;
        };
        float current_balance = investorCashBalanceHistory.getBalance();
        List<InvestorCashTransactionEntity> investorCashTransactionEntity = investorCashTransactionRepository.findTransactionsByDateTypeAndInvestor(keycloakService.getInvestorIdFromToken(), startDate, endDate);
        String refIdsStr = "";
        for (InvestorCashTransactionEntity transaction : investorCashTransactionEntity) {
            if (transaction.getRefId() != null) {
                // refIdList.add(transaction.getRefId());
                if(refIdsStr.equals("")){
                    refIdsStr += transaction.getRefId();
                }
                else{
                    refIdsStr += "," + transaction.getRefId();
                }
            }
        }
        ResponseBuilder<Map<String, Integer>> responseRefIds = tradingClient.getProductIdsWithOrderIds("Bearer " + keycloakService.getToken(),refIdsStr);
        List<CashTransactionHistoryRes> cashTransactionHistoryRes = new ArrayList<>();
        for (InvestorCashTransactionEntity transaction : investorCashTransactionEntity){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime combinedDateTime = LocalDateTime.of(
                transaction.getTranDate(),
                transaction.getTranTime().toLocalTime()
            );
            String formattedDateTime = combinedDateTime.format(formatter);

            CashTransactionHistoryRes cashTransactionHistoryRes1 = new CashTransactionHistoryRes();
            
            cashTransactionHistoryRes1.setCreatedDate(formattedDateTime);
            cashTransactionHistoryRes1.setTranType(transaction.getTranType());
            cashTransactionHistoryRes1.setTranAmount(BigDecimal.valueOf(transaction.getTranAmount()).setScale(2, RoundingMode.HALF_UP));
            cashTransactionHistoryRes1.setDescription(transaction.getDescription());
            cashTransactionHistoryRes1.setProductId(transaction.getRefId() != null ? responseRefIds.getResult().get(String.valueOf(transaction.getRefId())) : null);
            cashTransactionHistoryRes1.setOpr(transaction.getOpr());
            
            if ("+".equals(transaction.getOpr())) {
                current_balance += transaction.getTranAmount().floatValue();

            } else {
                current_balance -= transaction.getTranAmount().floatValue();
            }
            cashTransactionHistoryRes1.setBalanceAfTrans(BigDecimal.valueOf(current_balance).setScale(2, RoundingMode.HALF_UP));
            cashTransactionHistoryRes.add(cashTransactionHistoryRes1);
    }
    cashTransactionHistoryRes.removeIf(cashTransactionHistoryRes1 -> 
        !cashTransactionHistory.getTranType().contains(cashTransactionHistoryRes1.getTranType())
    );
    // List<CashTransactionHistoryRes> paginatedList = new ArrayList<>();
    Long totalElements = Long.valueOf(cashTransactionHistoryRes.size());
    Integer totalPages = (int) Math.ceil((double) totalElements / size);
    int fromIndex = page * size;
    int toIndex = Math.min(fromIndex + size, cashTransactionHistoryRes.size());
    List<CashTransactionHistoryRes> paginatedList = cashTransactionHistoryRes.subList(fromIndex, toIndex);
    Collections.reverse(paginatedList);
    CashTransactionHistoryPaging cashTransactionHistoryPaging = new CashTransactionHistoryPaging();
    cashTransactionHistoryPaging.setContent(paginatedList);
    cashTransactionHistoryPaging.setTotalElements(totalElements);
    cashTransactionHistoryPaging.setTotalPages(totalPages);

    return cashTransactionHistoryPaging;
    
    }
}