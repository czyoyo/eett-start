package com.example.eztask.controller;

import com.example.eztask.common.CommonResponse;
import com.example.eztask.dto.payment.PaymentRequestDto;
import com.example.eztask.dto.payment.PrePaymentRequestDto;
import com.example.eztask.enums.ResponseCode;
import com.example.eztask.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    // 결제요청 전 데이터 저장
    @PostMapping("/prePaymentRequest")
    public CommonResponse<Object> prePaymentRequest(@RequestBody @Valid PrePaymentRequestDto prePaymentRequestDto) {

        paymentService.prePaymentRequest(prePaymentRequestDto);

        return CommonResponse.builder()
            .message(ResponseCode.SUCCESS.getMessage())
            .code(ResponseCode.SUCCESS.getCode())
            .build();
    }

    // 결제정보 수신
    @PostMapping("/paymentRequest")
    public CommonResponse<Object> paymentRequest(@RequestBody @Valid PaymentRequestDto paymentRequestDto) {
        paymentService.pointPaymentRequest(paymentRequestDto);

        return CommonResponse.builder()
            .message(ResponseCode.SUCCESS.getMessage())
            .code(ResponseCode.SUCCESS.getCode())
            .build();
    }


}
