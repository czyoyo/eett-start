package com.example.eztask.repository.payment;

import com.example.eztask.entity.payment.PrePaymentData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrePaymentDataRepository extends JpaRepository<PrePaymentData, Long> {
    boolean existsByOrderId(String orderId);
    PrePaymentData findByOrderId(String orderId);
}
