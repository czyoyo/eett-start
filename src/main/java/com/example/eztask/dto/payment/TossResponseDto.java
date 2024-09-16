package com.example.eztask.dto.payment;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TossResponseDto {

    private String mId;
    private String version;
    private String paymentKey; // 결제 고유 번호
    private String status; // 결제 상태
    private String lastTransactionKey; // 최종 결제 키
    private String orderId; // 주문 번호
    private String orderName; // 주문 명
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    private boolean useEscrow;
    private boolean cultureExpense;
    private String method; // 결제 수단
    private CardInfo card;
    private VirtualAccountInfo virtualAccount;
    private TransferInfo transfer;
    private MobilePhoneInfo mobilePhone;
    private GiftCertificateInfo giftCertificate;
    private CashReceiptInfo cashReceipt;
    private List<CashReceiptInfo> cashReceipts;
    private DiscountInfo discount;
    private List<CancelInfo> cancels;
    private String secret;
    private String type;
    private EasyPayInfo easyPay;
    private String country;
    private FailureInfo failure; // 결제 실패 정보
    private boolean isPartialCancelable;
    private ReceiptInfo receipt;
    private CheckoutInfo checkout;
    private String currency; // 결제 통화
    private int totalAmount; // 총 결제 금액
    private int balanceAmount; // 잔액
    private int suppliedAmount; // 공급액
    private int vat; // 부가세
    private int taxFreeAmount; // 비과세

    @Data
    public static class CardInfo {
        private String issuerCode;
        private String acquirerCode;
        private String number;
        private int installmentPlanMonths;
        private boolean isInterestFree;
        private String interestPayer;
        private String approveNo;
        private boolean useCardPoint;
        private String cardType;
        private String ownerType;
        private String acquireStatus;
        private int amount;
    }

    @Data
    public static class VirtualAccountInfo {
        private String accountNumber;
        private String bankCode;
        private LocalDateTime dueDate;
        private String refundStatus;
        private boolean expired;
        private String settlementStatus;
    }

    @Data
    public static class TransferInfo {
        private String bankCode;
        private String settlementStatus;
    }

    @Data
    public static class MobilePhoneInfo {
        private String customerMobilePhone;
        private String settlementStatus;
        private String receiptUrl;
    }

    @Data
    public static class GiftCertificateInfo {
        private String approveNo;
        private String settlementStatus;
    }

    @Data
    public static class CashReceiptInfo {
        private String type;
        private String receiptKey;
        private String issueNumber;
        private String receiptUrl;
        private int amount;
        private int taxFreeAmount;
    }

    @Data
    public static class DiscountInfo {
        private int amount;
    }

    @Data
    public static class CancelInfo {
        private String cancelAmount;
        private String cancelReason;
        private int taxFreeAmount;
        private int taxExemptionAmount;
        private int refundableAmount;
        private LocalDateTime canceledAt;
        private String transactionKey;
    }

    @Data
    public static class EasyPayInfo {
        private String provider;
        private int amount;
        private int discountAmount;
    }

    @Data
    public static class FailureInfo {
        private String code;
        private String message;
    }

    @Data
    public static class ReceiptInfo {
        private String url;
    }

    @Data
    public static class CheckoutInfo {
        private String url;
    }
}
