package com.apec_finance.cash.model;

import java.util.List;
import lombok.Data;

@Data
public class CashTransactionHistoryPaging {
    private List<CashTransactionHistoryRes> content;
    private Long totalElements;
    private Integer totalPages;
}