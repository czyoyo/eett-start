package com.example.eztask.payment;

import com.example.eztask.dto.payment.TossRequestDto;
import com.example.eztask.dto.payment.TossResponseDto;
import com.example.eztask.dto.processor.PaymentRequest;
import com.example.eztask.dto.processor.PaymentResult;
import com.example.eztask.repository.payment.PaymentDataRepository;
import com.example.eztask.util.TossUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Component
public class TossPaymentProcessor implements PaymentProcessor {

    private static final Logger log = LoggerFactory.getLogger(TossPaymentProcessor.class);
    private final TossUtil tossUtil;
    private final ObjectMapper objectMapper;
    private final PaymentDataRepository paymentDataRepository;

    @Override
    public PaymentResult processPayment(PaymentRequest paymentRequest) {

        // toss 결제 요청
        String secretKey = tossUtil.encodeSecretKey();

        String url = "https://api.tosspayments.com/v1/payments/confirm";
        RestClient client = RestClient.builder()
            .requestFactory(new HttpComponentsClientHttpRequestFactory())
            .baseUrl(url)
            .defaultHeader("Authorization", secretKey)
            .defaultHeader("Content-Type", "application/json")
            .build();

        try {
            TossRequestDto tossRequestDto = TossRequestDto.builder()
                .orderId(paymentRequest.getOrderId())
                .amount(String.valueOf(paymentRequest.getAmount()))
                .paymentKey(paymentRequest.getPaymentKey())
                .build();

            String tossResponseJson = client.post()
                .body(tossRequestDto)
                .retrieve()
                .body(String.class);

            TossResponseDto tossResponseDto = objectMapper.readValue(tossResponseJson, TossResponseDto.class);

            return createPaymentResult(tossResponseDto);

        } catch (Exception e) {
            log.error("결제 요청 중 오류 발생", e);
        }
        return null;
    }

    private PaymentResult createPaymentResult(TossResponseDto tossResponseDto) {
        if("DONE".equals(tossResponseDto.getStatus())) {
            return PaymentResult.success
                (
                    tossResponseDto.getOrderId(), // 주문 ID
                    tossResponseDto.getMId(), // 프로바이더
                    tossResponseDto.getPaymentKey() // 결제 고유 번호
                    , tossResponseDto.getMethod() // 결제 수단
                    , String.valueOf(tossResponseDto.getTotalAmount()) // 결제 금액
                    , tossResponseDto.getCurrency() // 결제 통화
                );
        } else {
            return PaymentResult.fail(
                tossResponseDto.getOrderId(),
                tossResponseDto.getMId(),
                tossResponseDto.getFailure().getCode(),
                tossResponseDto.getFailure().getMessage()
            );
        }
    }

}
