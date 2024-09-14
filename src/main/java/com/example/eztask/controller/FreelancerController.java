package com.example.eztask.controller;


import com.example.eztask.common.CommonResponse;
import com.example.eztask.dto.freelancer.FreelancerDto;
import com.example.eztask.enums.ResponseCode;
import com.example.eztask.service.FreelancerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/freelancer")
public class FreelancerController {

    private final FreelancerService freelancerService;

    //:TODO 프리랜서 프로필 목록 조회
    @GetMapping()
    public CommonResponse<Page<FreelancerDto>> getFreelancerProfileList(Pageable pageable) {
        Page<FreelancerDto> freelancerProfileList = freelancerService.getFreelancerProfileList(pageable);

        return CommonResponse.<Page<FreelancerDto>>builder()
            .code(ResponseCode.SUCCESS.getCode())
            .message(ResponseCode.SUCCESS.getMessage())
            .data(freelancerProfileList)
            .build();
    }

    // :TODO 프리랜서 단일 프로필 조회 (조회수 업데이트)
    @GetMapping(path = "/profiles/{id}")
    public CommonResponse<FreelancerDto> getFreelancerProfile(@PathVariable("id") Long id) {

        FreelancerDto freelancerProfile = freelancerService.getFreelancerProfile(id);

        return CommonResponse.<FreelancerDto>builder()
            .code(ResponseCode.SUCCESS.getCode())
            .message(ResponseCode.SUCCESS.getMessage())
            .data(freelancerProfile)
            .build();
    }

    // :TODO 입력 없이 간단하게 랜덤 프리랜서 생성(테스트 용 API)
    @PostMapping("/create")
    public CommonResponse<Long> createFreelancer() {

        // 입력 받지 않고 랜덤 프리랜서 생성
        Long id = freelancerService.createFreelancer();

        return CommonResponse.<Long>builder()
            .code(ResponseCode.SUCCESS.getCode())
            .message(ResponseCode.SUCCESS.getMessage())
            .data(id)
            .build();
    }




}
