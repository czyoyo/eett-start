package com.example.eztask.dto.processor;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResult {

    private final boolean success; // 결제 성공 여부
    private final String provider; // 프로바이더
    private final String orderId; // 주문 ID
    private final String transactionId; // 결제 고유 번호
    private final String paymentMethod; // 결제 수단
    private final String amount; // 결제 금액
    private final String currency; // 결제 통화
    private final String errorCode; // 결제 실패 코드
    private final String errorMessage; // 결제 실패 메시지

    @Builder
    public static PaymentResult success(String orderId, String provider, String transactionId, String paymentMethod, String amount, String currency) {
        return PaymentResult.builder()
                .success(true)
                .orderId(orderId) // 주문 ID
                .provider(provider) // 프로바이더
                .transactionId(transactionId) // 결제 고유 번호
                .paymentMethod(paymentMethod) // 결제 수단
                .amount(amount) // 결제 금액
                .currency(currency) // 결제 통화
                .build();
    }

    @Builder
    public static PaymentResult fail(String orderId, String provider, String errorCode, String errorMessage) {
        return PaymentResult.builder()
                .success(false)
                .orderId(orderId) // 주문 ID
                .provider(provider) // 프로바이더
                .errorCode(errorCode) // 결제 실패 코드
                .errorMessage(errorMessage) // 결제 실패 메시지
                .build();
    }

}
