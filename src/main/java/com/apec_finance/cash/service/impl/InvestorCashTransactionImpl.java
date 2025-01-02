package com.apec_finance.cash.service.impl;

import com.apec_finance.cash.entity.InvestorCashTransactionEntity;
import com.apec_finance.cash.mapper.InvestorCashTransactionMapper;
import com.apec_finance.cash.model.CreateCashTransaction;
import com.apec_finance.cash.model.InvestorBankAcc;
import com.apec_finance.cash.model.InvestorCashBalance;
import com.apec_finance.cash.repository.InvestorCashBalanceRepository;
import com.apec_finance.cash.repository.InvestorCashTransactionRepository;
import com.apec_finance.cash.service.AppClient;
import com.apec_finance.cash.service.InvestorCashTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.apec_finance.cash.service.KeycloakService;
import com.apec_finance.cash.service.InvestorCashBalanceService;
import com.apec_finance.cash.entity.CsInvestorCashBalanceEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.apec_finance.cash.model.RsInvestorBankAcc;

@Service
@RequiredArgsConstructor
public class InvestorCashTransactionImpl implements InvestorCashTransactionService {
    private final InvestorCashTransactionRepository investorCashTransactionRepository;
    private final InvestorCashTransactionMapper investorCashTransactionMapper;
    private final AppClient appClient;
    private final KeycloakService keycloakService;
    private final InvestorCashBalanceRepository investorCashBalanceRepository;

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
        CsInvestorCashBalanceEntity csInvestorCashBalanceEntity = investorCashBalanceRepository.findByInvestorIdAndStatus(keycloakService.getInvestorIdFromToken(), "A");
        float currentBalance = csInvestorCashBalanceEntity.getBalance();
        System.out.println("currentBalance: " + currentBalance);
        if (currentBalance < createCashTransaction.getTranAmount().floatValue()) {
            return;
        }
        float currentHoldBalance = csInvestorCashBalanceEntity.getHoldBalance();
        csInvestorCashBalanceEntity.setBalance(currentBalance - createCashTransaction.getTranAmount().floatValue());
        csInvestorCashBalanceEntity.setHoldBalance(currentHoldBalance + createCashTransaction.getTranAmount().floatValue());
        investorCashBalanceRepository.save(csInvestorCashBalanceEntity);
        investorCashTransactionRepository.save(investorCashTransactionEntity);
    }

    public void verify_transaction(Integer transaction_id){
        
    }
}