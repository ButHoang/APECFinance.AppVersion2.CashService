package com.apec_finance.cash.mapper;

import com.apec_finance.cash.entity.InvestorCashTransactionEntity;
import com.apec_finance.cash.model.CreateCashTransaction;
import org.mapstruct.Mapper;

@Mapper
public interface InvestorCashTransactionMapper {
    InvestorCashTransactionEntity toEntity(CreateCashTransaction createCashTransaction);
}
