package com.apec_finance.cash.service.impl;

import com.apec_finance.cash.entity.CsInvestorCashTransactionEntity;
import com.apec_finance.cash.exception.validate.ValidationException;
import com.apec_finance.cash.mapper.InvestorCashBalanceMapper;
import com.apec_finance.cash.model.InvestorCashBalance;
import com.apec_finance.cash.model.InvestorCashTransaction;
import com.apec_finance.cash.repository.InvestorCashTransactionRepository;
import com.apec_finance.cash.service.InvestorCashTransactionService;
import com.apec_finance.cash.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InvestorCashTransactionServiceImpl implements InvestorCashTransactionService {
    private final InvestorCashTransactionRepository investorCashTransactionRepository;

    @Override
    public String addCashTransaction(Long investorId ,BigDecimal tranAmount , int tran_count) {

        LocalDate currentDate = LocalDate.now();
        // int currentDay = currentDate.getDayOfMonth();
        // int currentMonth = currentDate.getMonthValue();
        // int currentYear = currentDate.getYear();
        String dayString = String.valueOf(currentDate.getDayOfMonth());
        String monthString = String.valueOf(currentDate.getMonthValue());
        String yearString = String.valueOf(currentDate.getYear());


        String tran_no = "IN" + yearString + monthString + dayString ;

        investorCashTransactionRepository.addCashTransactionEntity(investorId, tran_no , "IN" , "+" , tranAmount, "A");
        
        // if (cashBalance == null) throw new ValidationException("User Id not found");
        // InvestorCashBalance rs = new InvestorCashBalance();
        // BigDecimal totalBalance = BigDecimal.valueOf(cashBalance.getBalance() + cashBalance.getHoldBalance());
        // rs.setTotalBalance(setTotalBalance(totalBalance));
        // String rs = "Success";
        return "success" ;
    }

    public BigDecimal setTotalBalance(BigDecimal totalBalance) {
        return totalBalance.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public int countTransactionToday() {
        return investorCashTransactionRepository.countTransactionToday();
    }

    
}


