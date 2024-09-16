package com.example.eztask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.eztask.dto.payment.PaymentRequestDto;
import com.example.eztask.dto.payment.PrePaymentRequestDto;
import com.example.eztask.dto.processor.PaymentRequest;
import com.example.eztask.dto.processor.PaymentResult;
import com.example.eztask.entity.freelancer.Freelancer;
import com.example.eztask.entity.payment.Coupon;
import com.example.eztask.entity.payment.PrePaymentData;
import com.example.eztask.payment.PaymentProcessor;
import com.example.eztask.repository.freelancer.FreelancerRepository;
import com.example.eztask.repository.payment.CouponRepository;
import com.example.eztask.repository.payment.PaymentDataRepository;
import com.example.eztask.repository.payment.PrePaymentDataRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PrePaymentDataRepository prePaymentDataRepository;

    @Mock
    private FreelancerRepository freelancerRepository;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private PaymentProcessor paymentProcessor;

    @Mock
    private PaymentDataRepository paymentDataRepository;

    @InjectMocks
    private PaymentService paymentService;


    @Test
    @DisplayName("사전 결제 요청 성공")
    void prePaymentRequest_Success() {
        // Given
        PrePaymentRequestDto requestDto = PrePaymentRequestDto
            .builder()
            .freelancerId(1L)
            .orderId("orderId")
            .amount("10000")
            .build();

        Freelancer freelancer = Freelancer.builder()
            .id(1L)
            .name("name")
            .detailViewCount(0)
            .build();

        // 프리랜서 조회수가 있게 하기 위해
        when(prePaymentDataRepository.existsByOrderId("orderId")).thenReturn(false);
        // 프리랜서 조회수가 있게 하기 위해
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));

        // When & Then
        // 예외가 발생하지 않으면 성공
        assertDoesNotThrow(() -> paymentService.prePaymentRequest(requestDto));
    }

    @Test
    @DisplayName("이미 결제 요청된 주문이 있을 때 - DB 에 동일한 orderId 가 존재할 때")
    void prePaymentRequest_OrderAlreadyExists() {
        // Given
        PrePaymentRequestDto requestDto = PrePaymentRequestDto
            .builder()
            .freelancerId(1L)
            .orderId("orderId")
            .amount("10000")
            .build();

        when(prePaymentDataRepository.existsByOrderId("orderId")).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> paymentService.prePaymentRequest(requestDto));
        assertEquals("이미 결제 요청된 주문입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("프리랜서 없을 때")
    void prePaymentRequest_InvalidAmount() {
        // Given
        PrePaymentRequestDto requestDto = PrePaymentRequestDto
            .builder()
            .freelancerId(1L)
            .orderId("orderId")
            .amount("10000")
            .build();

        // When & Then
        // exception 예상
        assertThrows(IllegalArgumentException.class, () -> paymentService.prePaymentRequest(requestDto));
    }

    @Test
    @DisplayName("포인트 추가 요청 성공")
    void pointPaymentRequest_Success() {

        // given
        PaymentRequestDto requestDto = PaymentRequestDto.builder()
            .freelancerId(1L)
            .orderId("orderId")
            .originalAmount("10000")
            .couponCode("couponCode")
            .build();

        Freelancer freelancer = Freelancer.builder()
            .id(1L)
            .name("name")
            .detailViewCount(0)
            .build();

        PrePaymentData prePaymentData = PrePaymentData.builder()
            .freelancer(freelancer)
            .orderId("orderId")
            .amount(new BigDecimal("8000"))
            .build();

        Coupon coupon = Coupon.builder()
            .code("couponCode")
            .discountRate(20)
            .build();

        PaymentResult paymentResult = PaymentResult.success("orderId", "toss", "transactionId", "CARD", "8000", "KRW");


        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(prePaymentDataRepository.findByOrderId("orderId")).thenReturn(prePaymentData);
        when(couponRepository.findByCode("couponCode")).thenReturn(coupon);
        when(paymentProcessor.processPayment(any(PaymentRequest.class))).thenReturn(paymentResult);

        // 저장부 없어도 성공
        when(paymentDataRepository.save(any())).thenReturn(null);

        // When & Then
        assertDoesNotThrow(() -> paymentService.pointPaymentRequest(requestDto));
    }

    @Test
    @DisplayName("포인트 추가 요청 실패 - 프리랜서가 없을 때")
    void pointPaymentRequest_FreelancerNotFound() {

        // given
        PaymentRequestDto requestDto = PaymentRequestDto.builder()
            .freelancerId(1L)
            .orderId("orderId")
            .originalAmount("10000")
            .couponCode("couponCode")
            .build();

        when(freelancerRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> paymentService.pointPaymentRequest(requestDto));
        assertEquals("프리랜서 정보가 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("포인트 추가 요청 실패 - 사전 저장 결제정보 데이터가 없을 때")
    void pointPaymentRequest_PrePaymentDataNotFound() {

        // given
        PaymentRequestDto requestDto = PaymentRequestDto.builder()
            .freelancerId(1L)
            .orderId("orderId")
            .originalAmount("10000")
            .couponCode("couponCode")
            .build();

        Freelancer freelancer = Freelancer.builder()
            .id(1L)
            .name("name")
            .detailViewCount(0)
            .build();

        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(prePaymentDataRepository.findByOrderId("orderId")).thenReturn(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> paymentService.pointPaymentRequest(requestDto));
        assertEquals("결제 요청 데이터가 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("쿠폰 입력 했으나 DB에 없을때 함수작동 테스트")
    void applyCouponDiscount_NoCoupon() {
        // given
        BigDecimal originalAmount = new BigDecimal("10000");
        Coupon coupon = Coupon.builder()
            .code("couponCode")
            .discountRate(20)
            .build();

        when(couponRepository.findByCode("couponCode")).thenReturn(null);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> paymentService.calculateTotalAmount(originalAmount, coupon.getCode()));
    }

    @Test
    @DisplayName("쿠폰 할인율이 100일때 함수작동 테스트")
    void applyCouponDiscount_DiscountRate100() {
        // given
        BigDecimal originalAmount = new BigDecimal("10000");
        Coupon coupon = Coupon.builder()
            .code("couponCode")
            .discountRate(100)
            .build();

        when(couponRepository.findByCode("couponCode")).thenReturn(coupon);

        // when
        BigDecimal bigDecimal = paymentService.calculateTotalAmount(originalAmount, coupon.getCode());

        // then
        assertEquals(new BigDecimal("8000"), bigDecimal);
    }

    @Test
    @DisplayName("쿠폰 미입력 했을때 함수작동 테스트")
    void applyCouponDiscount_NoCouponCode() {
        // given
        BigDecimal originalAmount = new BigDecimal("10000");

        // when
        BigDecimal bigDecimal = paymentService.calculateTotalAmount(originalAmount, null);

        // then
        assertEquals(new BigDecimal("10000"), bigDecimal);
    }


    @Test
    @DisplayName(" 쿠폰 할인율이 0일때 함수작동 테스트")
    void applyCouponDiscount_DiscountRateZero() {
        // given
        BigDecimal originalAmount = new BigDecimal("10000");
        Coupon coupon = Coupon.builder()
            .code("couponCode")
            .discountRate(0)
            .build();

        when(couponRepository.findByCode("couponCode")).thenReturn(coupon);

        // when
        BigDecimal bigDecimal = paymentService.calculateTotalAmount(originalAmount, coupon.getCode());

        // then
        assertEquals(new BigDecimal("10000"), bigDecimal);
    }

    @Test
    @DisplayName("쿠폰 할인율이 20일때, 가격이 10만원 일때 최대 2천원 할인 되는지 함수작동 테스트")
    void applyCouponDiscount_DiscountRate20() {
        // given
        BigDecimal originalAmount = new BigDecimal("100000");
        Coupon coupon = Coupon.builder()
            .code("couponCode")
            .discountRate(20)
            .build();

        when(couponRepository.findByCode("couponCode")).thenReturn(coupon);

        // when
        BigDecimal bigDecimal = paymentService.calculateTotalAmount(originalAmount, coupon.getCode());

        // then
        assertEquals(new BigDecimal("98000"), bigDecimal);
    }







}
