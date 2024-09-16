package com.example.eztask.dto.processor;

import com.example.eztask.dto.payment.PaymentRequestDto;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    private String orderId; // 주문 번호
    private BigDecimal amount; // 결제 금액
    private String currency; // 결제 통화
    private String paymentMethod; // 결제 수단
    private String paymentKey; // 결제 키
    private Long freelancerId; // 프리랜서 아이디

    public static PaymentRequest from (PaymentRequestDto dto, BigDecimal discountAmount) {
        return PaymentRequest.builder()
                .orderId(dto.getOrderId())
                .amount(discountAmount)
                .currency("KRW")
                .paymentMethod("CARD")
                .paymentKey(dto.getPaymentKey())
                .freelancerId(dto.getFreelancerId())
                .build();
    }

}
