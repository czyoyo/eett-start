package com.example.eztask.entity.payment;

import com.example.eztask.entity.freelancer.Freelancer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "pre_payment_data")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Comment("사전 결제 데이터")
public class PrePaymentData {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Comment("인덱스")
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED")
    private long id;

    @JoinColumn(name = "request_user_id", columnDefinition = "BIGINT UNSIGNED", nullable = false, foreignKey = @jakarta.persistence.ForeignKey(name = "FK_PRE_PAYMENT_DATA_REQUEST_USER_ID"))
    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("프리랜서 인덱스")
    private Freelancer freelancer;

    @Column(name = "order_id", columnDefinition = "VARCHAR(100)", nullable = false, unique = true)
    private String orderId;

    @Column(name = "amount", columnDefinition = "INT", nullable = false)
    private BigDecimal amount;


}
