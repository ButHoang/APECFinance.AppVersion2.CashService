package com.apec_finance.cash.model;
import lombok.Data;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender.Size;
import com.apec_finance.cash.comon.BaseSearch;

import java.util.List;

@Data
public class CashTransactionHistory extends BaseSearch {
    String dateStart;
    String dateEnd;
    List<String> tranType;

}
