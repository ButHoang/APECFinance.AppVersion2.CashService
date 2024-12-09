package com.apec_finance.cash.service.impl;

import com.apec_finance.cash.entity.AssetEntity;
import com.apec_finance.cash.exception.validate.ValidationException;
import com.apec_finance.cash.mapper.AssetMapper;
import com.apec_finance.cash.model.Asset;
import com.apec_finance.cash.repository.AssetRepository;
import com.apec_finance.cash.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {
    private final AssetMapper assetMapper;
    private final AssetRepository assetRepository;
    @Override
    public Asset getTotalAsset(Long accountId) {
        AssetEntity asset = assetRepository.findById(accountId)
                .orElseThrow(() -> new ValidationException("Username is already taken"));
        Asset rs = assetMapper.getDTO(asset);
        return rs;
    }
}
