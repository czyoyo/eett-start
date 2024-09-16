package com.example.eztask.service;

import com.example.eztask.dto.payment.PaymentRequestDto;
import com.example.eztask.dto.payment.PrePaymentRequestDto;
import com.example.eztask.entity.freelancer.Freelancer;
import com.example.eztask.entity.payment.Coupon;
import com.example.eztask.entity.payment.PaymentData;
import com.example.eztask.entity.payment.PrePaymentData;
import com.example.eztask.payment.PaymentProcessor;
import com.example.eztask.dto.processor.PaymentRequest;
import com.example.eztask.dto.processor.PaymentResult;
import com.example.eztask.payment.TossPaymentProcessor;
import com.example.eztask.repository.freelancer.FreelancerRepository;
import com.example.eztask.repository.payment.CouponRepository;
import com.example.eztask.repository.payment.PaymentDataRepository;
import com.example.eztask.repository.payment.PrePaymentDataRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PaymentService {

    private final PrePaymentDataRepository prePaymentDataRepository;
    private final PaymentDataRepository paymentDataRepository;
    private final FreelancerRepository freelancerRepository;
    private final CouponRepository couponRepository;
    private final PaymentProcessor paymentProcessor;

    public PaymentService(
        PrePaymentDataRepository prePaymentDataRepository,
        PaymentDataRepository paymentDataRepository,
        FreelancerRepository freelancerRepository,
        CouponRepository couponRepository,
        PaymentProcessor paymentProcessor) {
        this.prePaymentDataRepository = prePaymentDataRepository;
        this.paymentDataRepository = paymentDataRepository;
        this.freelancerRepository = freelancerRepository;
        this.couponRepository = couponRepository;
        this.paymentProcessor = paymentProcessor;
    }

    @Transactional
    public void prePaymentRequest(PrePaymentRequestDto prePaymentRequestDto) {

        if(prePaymentDataRepository.existsByOrderId(prePaymentRequestDto.getOrderId())) {
            throw new IllegalArgumentException("이미 결제 요청된 주문입니다.");
        }

        try{
            Freelancer freelancer = freelancerRepository.findById(prePaymentRequestDto.getFreelancerId())
                .orElseThrow(() -> new IllegalArgumentException("프리랜서 정보가 없습니다."));

            PrePaymentData prePaymentData = PrePaymentData.builder()
                .freelancer(freelancer)
                .orderId(prePaymentRequestDto.getOrderId())
                .amount(new BigDecimal(prePaymentRequestDto.getAmount()))
                .build();

            prePaymentDataRepository.save(prePaymentData);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("금액은 숫자로 입력해주세요.");
        } catch (Exception e) {
            log.error("사전 결제 요청 중 오류 발생", e);
            throw new IllegalArgumentException("알 수 없는 오류가 발생했습니다.");
        }

    }

    @Transactional
    public void pointPaymentRequest(PaymentRequestDto paymentRequestDto) {

        // 회원 정보 존재 하는지 확인
        Freelancer freelancer = freelancerRepository.findById(paymentRequestDto.getFreelancerId())
            .orElseThrow(() -> new IllegalArgumentException("프리랜서 정보가 없습니다."));

        // 결제 요청 데이터가 있는지 확인
        PrePaymentData prePaymentData = prePaymentDataRepository.findByOrderId(paymentRequestDto.getOrderId());
        if(prePaymentData == null) {
            throw new IllegalArgumentException("결제 요청 데이터가 없습니다.");
        }

        // 원래 금액과 할인 금액
        BigDecimal originalAmount = new BigDecimal(paymentRequestDto.getOriginalAmount());
        BigDecimal discountAmount = applyCouponDiscount(originalAmount, paymentRequestDto.getCouponCode());
        discountAmount = discountAmount.setScale(0, BigDecimal.ROUND_HALF_UP);


        // 가격 비교
        if(!prePaymentData.getAmount().equals(discountAmount)) {
            log.info("원래 금액: {}, 할인 금액: {}", prePaymentData.getAmount(), discountAmount);
            throw new IllegalArgumentException("결제 금액이 일치하지 않습니다.");
        }

        // 결제 요청
        PaymentRequest tossPaymentRequest = PaymentRequest.builder()
            .orderId(paymentRequestDto.getOrderId())
            .amount(discountAmount)
            .paymentKey(paymentRequestDto.getPaymentKey())
            .build();

        PaymentResult result = paymentProcessor.processPayment(tossPaymentRequest);

        // 결제 데이터 저장
        savePaymentData(result, freelancer, originalAmount, discountAmount, paymentRequestDto.getCouponCode());

        // 성공하면 프리랜서 포인트 업데이트
        if(result.isSuccess()) {
            updateFreelancerPoint(freelancer, discountAmount);
        }

    }

    // 프리랜서의 포인트 업데이트
    private void updateFreelancerPoint(Freelancer freelancer, BigDecimal amount) {
        freelancer.addPoint(amount);
    }

    // 결제 데이터 저장
    private void savePaymentData(
        PaymentResult result,
        Freelancer freelancer,
        BigDecimal originalAmount,
        BigDecimal discountedAmount,
        String couponCode
    ) {
        PaymentData paymentData = PaymentData.builder()
            .freelancer(freelancer)
            .paymentProvider(result.getProvider())
            .paymentKey(result.getTransactionId())
            .status(result.isSuccess() ? "COMPLETED" : "FAILED")
            .method(result.getPaymentMethod())
            .orderId(result.getOrderId())
            .currency(result.getCurrency())
            .originalAmount(BigDecimal.valueOf(originalAmount.intValue()))
            .totalAmount(BigDecimal.valueOf(discountedAmount.intValue()))
            .discountAmount(BigDecimal.valueOf(originalAmount.subtract(discountedAmount).intValue()))
            .couponCode(couponCode)
            .requestedAt(LocalDateTime.now())
            .approvedAt(result.isSuccess() ? LocalDateTime.now() : null)
            .build();

        paymentDataRepository.save(paymentData);
    }

    private BigDecimal applyCouponDiscount(BigDecimal originalAmount, String couponCode) {
        if(couponCode == null || couponCode.isEmpty()) {
            return originalAmount;
        }

        Coupon coupon = couponRepository.findByCode(couponCode);
        if(coupon == null) {
            throw new IllegalArgumentException("쿠폰 정보가 없습니다.");
        }

        // 할인 금액은 최대 2,000
        BigDecimal discountAmount = originalAmount.multiply(BigDecimal.valueOf(coupon.getDiscountRate()).divide(BigDecimal.valueOf(100))); // 할인 금액
        BigDecimal maxDiscount = BigDecimal.valueOf(2000); // 최대 할인 금액

        discountAmount = discountAmount.min(maxDiscount);

        return originalAmount.subtract(discountAmount);
    }

    public BigDecimal calculateTotalAmount(BigDecimal originalAmount, String couponCode) {
        return applyCouponDiscount(originalAmount, couponCode);
    }


}
