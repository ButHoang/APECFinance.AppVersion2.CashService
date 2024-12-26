package com.apec_finance.cash.entity;

import com.apec_finance.cash.comon.BaseEntity;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "cs_investor_cash_transaction")
public class InvestorCashTransactionEntity extends BaseEntity {
    @Column(name = "investor_id", nullable = false)
    private Long investorId;

    @Column(nullable = false)
    private LocalDate tranDate; // Ngày giao dịch (yyyy-MM-dd)

    @Column(nullable = false)
    private LocalDateTime tranTime; // Thời gian giao dịch (bao gồm giờ phút giây)

    @Column(nullable = false, length = 50)
    private String tranNo; // Mã giao dịch

    @Column(nullable = false, length = 50)
    private String tranType; // Loại giao dịch

    @Column(nullable = false, length = 1)
    private String opr; // Toán tử +/-

    @Column(nullable = false)
    private Double tranAmount; // Số tiền giao dịch (bao gồm phí)

    private Double feeAmount; // Số tiền phí/thuế

    private Double finalAmount; // Số tiền chốt

    private Long refId; // ID của giao dịch tham chiếu

    private String refNo; // Mã giao dịch tham chiếu

    private String bankRefId; // Mã tham chiếu tới giao dịch của Bank Gateway

    @Column(length = 512)
    private String description; // Mô tả giao dịch

    @Column(nullable = false, length = 1)
    private String status; // Trạng thái giao dịch (P, A, R, C)

    private OffsetDateTime verifiedDate; // Thời gian xác nhận

    private String verifiedBy; // Người xác nhận

    private String bankAccount; // Tài khoản ngân hàng nhận

    private String bankCode; // Mã ngân hàng nhận

}
