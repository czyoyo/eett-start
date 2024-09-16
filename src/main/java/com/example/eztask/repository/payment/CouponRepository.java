package com.example.eztask.repository.payment;

import com.example.eztask.entity.payment.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Coupon findByCode(String code);

}
