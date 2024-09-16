package com.example.eztask.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TossRequestDto {

    private String orderId;
    private String amount;
    private String paymentKey;

}
