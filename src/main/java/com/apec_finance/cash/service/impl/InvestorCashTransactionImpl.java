package com.apec_finance.cash.service.impl;

import com.apec_finance.cash.entity.InvestorCashTransactionEntity;
import com.apec_finance.cash.mapper.InvestorCashTransactionMapper;
import com.apec_finance.cash.model.CreateCashTransaction;
import com.apec_finance.cash.repository.InvestorCashBalanceRepository;
import com.apec_finance.cash.repository.InvestorCashTransactionRepository;
import com.apec_finance.cash.service.AppClient;
import com.apec_finance.cash.service.InvestorCashTransactionService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import com.apec_finance.cash.model.CashTransactionHistoryRes;


import com.apec_finance.cash.model.RsInvestorBankAcc;
import com.apec_finance.cash.model.RsTransactionRange;
import com.apec_finance.cash.model.TransactionRange;
import com.apec_finance.cash.repository.InvestorCashBalanceHistoryRepository;
import com.apec_finance.cash.model.CashTransactionHistory;;

@Service
@RequiredArgsConstructor
public class InvestorCashTransactionImpl implements InvestorCashTransactionService {
    private final InvestorCashTransactionRepository investorCashTransactionRepository;
    private final InvestorCashTransactionMapper investorCashTransactionMapper;
    private final AppClient appClient;
    private final KeycloakService keycloakService;
    private final InvestorCashBalanceRepository investorCashBalanceRepository;
    private final InvestorCashBalanceHistoryRepository investorCashBalanceHistoryRepository;
    private final TradingClient tradingClient;

    @Override
    public void createCashTransaction(CreateCashTransaction createCashTransaction) {
        InvestorCashTransactionEntity investorCashTransactionEntity = investorCashTransactionMapper.toEntity(createCashTransaction);
        investorCashTransactionEntity.setTranNo(generateTransactionNumber(createCashTransaction.getTranType(), createCashTransaction.getTranDate()));
        investorCashTransactionRepository.save(investorCashTransactionEntity);
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
        System.out.println("Withdrawal transaction created successfully");
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
            System.out.println("Investor cash balance not found " + keycloakService.getInvestorIdFromToken());
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
                System.out.println("Transaction time is not valid");
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
        // System.out.println("Deposit transaction created successfully");
        try{
            CsInvestorCashBalanceEntity csInvestorCashBalanceEntity = investorCashBalanceRepository.findByInvestorIdAndStatus(keycloakService.getInvestorIdFromToken(), "A");
            float currentBalance = csInvestorCashBalanceEntity.getBalance();
            csInvestorCashBalanceEntity.setBalance(currentBalance + createCashTransaction.getTranAmount().floatValue());
            investorCashBalanceRepository.save(csInvestorCashBalanceEntity);
        }
        catch (Exception e){
            System.out.println("Investor cash balance not found " + keycloakService.getInvestorIdFromToken());
            return;
        }
        investorCashTransactionRepository.save(investorCashTransactionEntity);
        // RsTransactionRange transactionRangeslist = appClient.getTransactionRange();
    }
    

    public void verifyCashTransaction(Long transaction_id){
        InvestorCashTransactionEntity existCashTransaction = investorCashTransactionRepository.findById(transaction_id).orElse(null); 
        if (existCashTransaction == null) {
            System.out.println("Investor cash transaction not found");
            return;
        }
        existCashTransaction.setStatus("A");
        CsInvestorCashBalanceEntity investorCashBalance = investorCashBalanceRepository.findByInvestorIdAndStatus(existCashTransaction.getInvestorId(), "A");
        investorCashBalance.setHoldBalance(investorCashBalance.getHoldBalance() - existCashTransaction.getTranAmount().floatValue());
        investorCashBalanceRepository.save(investorCashBalance);
        investorCashTransactionRepository.save(existCashTransaction);
    }

    public List<CashTransactionHistoryRes> historyCashTransaction(CashTransactionHistory cashTransactionHistory){
        String dateStartString = cashTransactionHistory.getDateStart();
        LocalDate startDate = LocalDate.parse(dateStartString);
        String dateEndString = cashTransactionHistory.getDateEnd();
        LocalDate endDate = LocalDate.parse(dateEndString);
        CsInvestorCashBalanceHistoryEntity investorCashBalanceHistory = investorCashBalanceHistoryRepository.findByInvestorIdAndTradingDate( keycloakService.getInvestorIdFromToken()  , startDate );
        // , startDate
        // if (investorCashBalanceHistory == null) {
        //     System.out.println("Investor cash balance history not found");
        //     return ;
        // }
        float current_balance = investorCashBalanceHistory.getBalance();
        System.out.println(current_balance);
        List<InvestorCashTransactionEntity> investorCashTransactionEntity = investorCashTransactionRepository.findTransactionsByDateTypeAndInvestor(keycloakService.getInvestorIdFromToken(), startDate, endDate);
        // System.out.println(investorCashTransactionEntity );
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
        // System.out.println(responseRefIds.getResult());
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
            cashTransactionHistoryRes1.setTranAmount(BigDecimal.valueOf(transaction.getTranAmount()));
            cashTransactionHistoryRes1.setDescription(transaction.getDescription());
            cashTransactionHistoryRes1.setProductId(transaction.getRefId() != null ? responseRefIds.getResult().get(String.valueOf(transaction.getRefId())) : null);
            cashTransactionHistoryRes1.setOpr(transaction.getOpr());
            
            if ("+".equals(transaction.getOpr())) {
                current_balance += transaction.getTranAmount().floatValue();

            } else {
                current_balance -= transaction.getTranAmount().floatValue();
            }
            cashTransactionHistoryRes1.setBalanceAfTrans(BigDecimal.valueOf(current_balance));
            cashTransactionHistoryRes.add(cashTransactionHistoryRes1);
    }
    // System.out.println(cashTransactionHistory.getTranType());
    cashTransactionHistoryRes.removeIf(cashTransactionHistoryRes1 -> 
    !cashTransactionHistory.getTranType().contains(cashTransactionHistoryRes1.getTranType())
);

    return cashTransactionHistoryRes;
    }
}