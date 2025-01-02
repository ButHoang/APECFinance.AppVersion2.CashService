package com.apec_finance.cash.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateAsset {
    @NotNull
    private Long id;
}
