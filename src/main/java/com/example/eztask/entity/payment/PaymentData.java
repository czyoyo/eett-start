package com.example.eztask.entity.payment;

import com.example.eztask.entity.freelancer.Freelancer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "before_payment_data")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PaymentData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_id", nullable = false)
    @Comment("프리랜서 ID")
    private Freelancer freelancer;

    @Column(name = "payment_provider", columnDefinition = "VARCHAR(50)", nullable = false)
    @Comment("결제 프로바이더")
    private String paymentProvider;

    @Column(name = "payment_key", columnDefinition = "VARCHAR(100)", nullable = false)
    @Comment("결제 키")
    private String paymentKey;

    @Column(name = "status", columnDefinition = "VARCHAR(50)", nullable = false)
    @Comment("결제 상태")
    private String status;

    @Column(name = "method", columnDefinition = "VARCHAR(50)", nullable = false)
    @Comment("결제 수단")
    private String method;

    @Column(name = "order_id", columnDefinition = "VARCHAR(100)", nullable = false)
    @Comment("주문 ID")
    private String orderId;

    @Column(name = "currency", columnDefinition = "VARCHAR(10)", nullable = false)
    @Comment("통화")
    private String currency;

    @Comment("원래 금액")
    @Column(name = "original_amount", columnDefinition = "INT", nullable = false)
    private BigDecimal originalAmount;

    @Comment("총 금액")
    @Column(name = "total_amount", columnDefinition = "INT", nullable = false)
    private BigDecimal totalAmount;

    @Comment("할인 금액")
    @Column(name = "discount_amount", columnDefinition = "INT")
    private BigDecimal discountAmount;

    @Comment("사용된 쿠폰 코드")
    @Column(name = "coupon_code", columnDefinition = "VARCHAR(100)")
    private String couponCode;

    @Comment("결제 요청 시각")
    @Column(name = "requested_at", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime requestedAt;

    @Comment("결제 승인 시각")
    @Column(name = "approved_at", columnDefinition = "DATETIME")
    private LocalDateTime approvedAt;

    // 결제 제공자별 추가 데이터를 저장하기 위한 필드
    @Column(columnDefinition = "JSON")
    private String additionalData;
}
