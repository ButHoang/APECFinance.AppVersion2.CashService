package com.apec_finance.cash.mapper;

import com.apec_finance.cash.entity.AssetEntity;
import com.apec_finance.cash.model.Asset;
import org.mapstruct.Mapper;

@Mapper
public interface AssetMapper {
    Asset getDTO(AssetEntity assetEntity);
    AssetEntity getEntity(Asset asset);
}
