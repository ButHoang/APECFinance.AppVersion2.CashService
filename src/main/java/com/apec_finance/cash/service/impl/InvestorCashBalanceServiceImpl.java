package com.apec_finance.cash.service.impl;

import com.apec_finance.cash.entity.CsInvestorCashBalanceEntity;
import com.apec_finance.cash.exception.validate.ValidationException;
import com.apec_finance.cash.mapper.InvestorCashBalanceMapper;
import com.apec_finance.cash.model.InvestorCashBalance;
import com.apec_finance.cash.repository.InvestorCashBalanceRepository;
import com.apec_finance.cash.service.InvestorCashBalanceService;
import com.apec_finance.cash.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

@Service
@RequiredArgsConstructor
public class InvestorCashBalanceServiceImpl implements InvestorCashBalanceService {
    private final InvestorCashBalanceRepository investorCashBalanceRepository;

    @Override
    public InvestorCashBalance getCashBalance(Long investorId) {
        
        CsInvestorCashBalanceEntity cashBalance = investorCashBalanceRepository.findByInvestorIdAndStatus(investorId, "A");
        if (cashBalance == null) throw new ValidationException("User Id not found");
        InvestorCashBalance rs = new InvestorCashBalance();
        BigDecimal totalBalance = BigDecimal.valueOf(cashBalance.getBalance() + cashBalance.getHoldBalance());
        rs.setTotalBalance(setTotalBalance(totalBalance));
        return rs;
    }

    public BigDecimal setTotalBalance(BigDecimal totalBalance) {
        return totalBalance.setScale(2, RoundingMode.HALF_UP);
    }
}

