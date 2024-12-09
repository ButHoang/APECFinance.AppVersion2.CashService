package com.apec_finance.cash.service;

import com.apec_finance.cash.model.Asset;

public interface AssetService {
    Asset getTotalAsset(Long accountId);
}
