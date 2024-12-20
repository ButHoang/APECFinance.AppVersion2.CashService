package com.apec_finance.cash.mapper;

import com.apec_finance.cash.entity.CsInvestorCashBalanceEntity;
import com.apec_finance.cash.model.InvestorCashBalance;
import org.mapstruct.Mapper;

@Mapper
public interface InvestorCashBalanceMapper {
    InvestorCashBalance getDTO(CsInvestorCashBalanceEntity csInvestorCashBalanceEntity);
    CsInvestorCashBalanceEntity getEntity(InvestorCashBalance investorCashBalance);
}
