package com.apec_finance.cash.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionRange {

    @JsonProperty("grname")
    private String grName;

    @JsonProperty("varname")
    private String varName;

    @JsonProperty("varvalue")
    private String varValue;
}
