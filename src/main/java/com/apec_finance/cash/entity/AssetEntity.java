package com.apec_finance.cash.entity;

import com.apec_finance.cash.comon.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "asset")
public class AssetEntity extends BaseEntity {
    private Long total;
}
