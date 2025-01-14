package com.apec_finance.cash.model;
import lombok.Data;
import java.util.List;

@Data
public class CashTransactionHistory {
    String dateStart;
    String dateEnd;
    List<String> tranType;

}
