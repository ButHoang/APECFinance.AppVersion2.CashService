package com.apec_finance.cash.mapper;

import com.apec_finance.cash.entity.CsInvestorCashBalanceEntity;
import com.apec_finance.cash.model.InvestorCashBalance;
import com.apec_finance.cash.model.UpdateCashBalance;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InvestorCashBalanceMapper {
    InvestorCashBalance getDTO(CsInvestorCashBalanceEntity csInvestorCashBalanceEntity);
    CsInvestorCashBalanceEntity getEntity(InvestorCashBalance investorCashBalance);
    CsInvestorCashBalanceEntity updateEntity(UpdateCashBalance updateCashBalance, @MappingTarget CsInvestorCashBalanceEntity entity);
}
