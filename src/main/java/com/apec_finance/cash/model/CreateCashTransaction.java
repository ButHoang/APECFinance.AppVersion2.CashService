package com.apec_finance.cash.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class CreateCashTransaction {
    private LocalDate tranDate;
    private LocalDateTime tranTime;
    private String tranNo;
    private String tranType;
    private String opr;
    private Float tranAmount;
    private Long refId;
    private String refNo;
    private String status;

}
