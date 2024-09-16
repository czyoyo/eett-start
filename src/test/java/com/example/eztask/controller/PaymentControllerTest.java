package com.example.eztask.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.eztask.config.GlobalExceptionHandler;
import com.example.eztask.dto.payment.PaymentRequestDto;
import com.example.eztask.dto.payment.PrePaymentRequestDto;
import com.example.eztask.enums.ResponseCode;
import com.example.eztask.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(paymentController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setValidator(validator)
            .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("사전결제 요청 성공")
    void prePaymentRequest_Success() throws Exception {

        // given
        PrePaymentRequestDto requestDto = PrePaymentRequestDto.builder()
            .freelancerId(1L)
            .amount("1000")
            .orderId("order1")
            .build();

        // object to json
        String json = objectMapper.writeValueAsString(requestDto);

        // when -> then
        mockMvc.perform(post("/api/payment/prePaymentRequest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()))
            .andExpect(jsonPath("$.message").value(ResponseCode.SUCCESS.getMessage()));

        verify(paymentService).prePaymentRequest(any(PrePaymentRequestDto.class));
    }

    @Test
    @DisplayName("사전결제 요청 실패 - 잘못된 입력")
    void prePaymentRequest_InvalidArg() throws Exception {

        PrePaymentRequestDto requestDto = PrePaymentRequestDto.builder()
            .freelancerId(null) // 필수값 누락
            .amount("1000")
            .orderId("order1")
            .build();

        String json = objectMapper.writeValueAsString(requestDto);

        // when -> then
        mockMvc.perform(post("/api/payment/prePaymentRequest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            ).andExpect(status().isBadRequest());

        // 서비스단 호출되지 않았는지 확인 -> 호출되면 안됨
        verify(paymentService, never()).prePaymentRequest(any(PrePaymentRequestDto.class));
    }

    @Test
    @DisplayName("본 결제 요청 성공")
    void paymentRequest_Success() throws Exception {

        // given
        PaymentRequestDto requestDto = PaymentRequestDto.builder()
            .freelancerId(1L)
            .paymentKey("paymentKey")
            .orderId("order1")
            .amount("8000")
            .originalAmount("10000")
            .couponCode("couponCode")
            .build();

        // object to json
        String json = objectMapper.writeValueAsString(requestDto);

        // when -> then
        mockMvc.perform(post("/api/payment/paymentRequest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()))
            .andExpect(jsonPath("$.message").value(ResponseCode.SUCCESS.getMessage()));

        // 서비스단 호출 확인
        verify(paymentService).pointPaymentRequest(any(PaymentRequestDto.class));
    }

    @Test
    @DisplayName("본 결제 요청 실패 - 잘못된 입력")
    void paymentRequest_InvalidArg() throws Exception {

        PaymentRequestDto requestDto = PaymentRequestDto.builder()
            .freelancerId(null) // 필수값 누락
            .paymentKey("paymentKey")
            .orderId("order1")
            .amount("8000")
            .originalAmount("10000")
            .couponCode("couponCode")
            .build();

        String json = objectMapper.writeValueAsString(requestDto);

        // when -> then
        mockMvc.perform(post("/api/payment/paymentRequest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            ).andExpect(status().isBadRequest());

        // 서비스단 호출되지 않았는지 확인 -> 호출되면 안됨
        verify(paymentService, never()).pointPaymentRequest(any(PaymentRequestDto.class));
    }




}
