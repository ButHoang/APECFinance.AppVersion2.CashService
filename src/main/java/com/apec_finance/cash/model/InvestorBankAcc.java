package com.apec_finance.cash.model;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.apec_finance.cash.model.Bank;
import lombok.Data;

@Data
public class InvestorBankAcc {
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

    @JsonProperty("investor_id")
    private Long investorId;

    @JsonProperty("bank_account")
    private String bankAccount;

    @JsonProperty("bank_id")
    private Bank bankId;

    @JsonProperty("bank_branch")
    private String bankBranch;

    @JsonProperty("is_default")
    private String isDefault;


}
