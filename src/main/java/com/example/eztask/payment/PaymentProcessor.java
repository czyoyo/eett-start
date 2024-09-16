package com.example.eztask.payment;

import com.example.eztask.dto.processor.PaymentRequest;
import com.example.eztask.dto.processor.PaymentResult;

public interface PaymentProcessor {
    PaymentResult processPayment(PaymentRequest paymentRequest);
}
