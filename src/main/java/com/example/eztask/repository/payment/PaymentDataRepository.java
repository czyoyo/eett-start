package com.example.eztask.repository.payment;

import com.example.eztask.entity.payment.PaymentData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDataRepository extends JpaRepository<PaymentData, Long> {

}
