package com.example.eztask.entity.payment;

import com.example.eztask.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coupon", indexes = {
    @Index(name = "idx_coupon_code", columnList = "code")
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 쿠폰 이름
    @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false)
    private String name;

    // 쿠폰 코드
    @Column(name = "code", columnDefinition = "VARCHAR(100)", nullable = false)
    private String code;

    // 할인률 , 할인 금액은 최대 2000원 할인까지 가능
    @Column(name = "discount_rate", columnDefinition = "INT", nullable = false)
    private int discountRate;


}
