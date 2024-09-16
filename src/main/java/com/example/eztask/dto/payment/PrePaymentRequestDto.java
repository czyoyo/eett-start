package com.example.eztask.dto.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrePaymentRequestDto {


    @NotNull(message = "필수 데이터가 없습니다.")
    // 이 값의 경우 원래는 JWT 토큰에서 파싱해야 하나 테스트 코드이므로 이렇게 처리 하였습니다.
    private Long freelancerId;


    @NotBlank(message = "필수 데이터가 없습니다.")
    private String orderId;
    @NotBlank(message = "필수 데이터가 없습니다.")
    private String amount;


}
