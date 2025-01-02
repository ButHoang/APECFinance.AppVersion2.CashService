package com.apec_finance.cash.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

import javax.print.DocFlavor.STRING;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class Bank {
    private Long id;

    @JsonProperty("status")
    private String status;

    private Integer sort;

    @JsonProperty("user_created")
    private String userCreated;

    @JsonProperty("date_created")
    private OffsetDateTime dateCreated;

    @JsonProperty("user_updated")
    private String userUpdated;

    @JsonProperty("date_updated")
    private OffsetDateTime dateUpdated;

    @JsonProperty("bank_code")
    private String bankCode;

    @JsonProperty("bankname_vn")
    private String bankNameVn;

    @JsonProperty("bankname_en")
    private String bankNameEn;

    @JsonProperty("bincode")
    private String binCode;

   
}


