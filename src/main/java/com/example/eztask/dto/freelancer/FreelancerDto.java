package com.example.eztask.dto.freelancer;

import com.example.eztask.entity.freelancer.Freelancer;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FreelancerDto {

    private Long id;
    private String name;
    private Long detailViewCount;
    private LocalDateTime createdAt;


    public static FreelancerDto convertToDto(Freelancer freelancer) {
        return FreelancerDto.builder()
            .id(freelancer.getId())
            .name(freelancer.getName())
            .detailViewCount(freelancer.getDetailViewCount())
            .createdAt(freelancer.getCreatedAt())
            .build();
    }

}
