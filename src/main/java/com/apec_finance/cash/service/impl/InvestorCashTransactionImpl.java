package com.apec_finance.cash.service.impl;

import com.apec_finance.cash.entity.InvestorCashTransactionEntity;
import com.apec_finance.cash.mapper.InvestorCashTransactionMapper;
import com.apec_finance.cash.model.CreateCashTransaction;
import com.apec_finance.cash.repository.InvestorCashTransactionRepository;
import com.apec_finance.cash.service.InvestorCashTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvestorCashTransactionImpl implements InvestorCashTransactionService {
    private final InvestorCashTransactionRepository investorCashTransactionRepository;
    private final InvestorCashTransactionMapper investorCashTransactionMapper;
    @Override
    public void createCashTransaction(CreateCashTransaction createCashTransaction) {
        InvestorCashTransactionEntity investorCashTransactionEntity = investorCashTransactionMapper.toEntity(createCashTransaction);
        investorCashTransactionRepository.save(investorCashTransactionEntity);
    }
}
