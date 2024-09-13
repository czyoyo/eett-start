package com.example.eztask.service;

import com.example.eztask.dto.freelancer.FreelancerDto;
import com.example.eztask.entity.freelancer.Freelancer;
import com.example.eztask.repository.freelancer.FreelancerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FreelancerService {

    private final FreelancerRepository freelancerRepository;


    @Transactional(readOnly = true)
    public Page<FreelancerDto> getFreelancerProfileList(Pageable pageable) {
        // pageable 의 size 가 10 초과인 경우 예외처리
        if (pageable.getPageSize() > 10) {
            throw new IllegalArgumentException("페이지 사이즈는 10 이하로 설정해주세요.");
        }
        Page<Freelancer> allFreelancerList = freelancerRepository.findAllFreelancerList(pageable);
        return allFreelancerList.map(FreelancerDto::convertToDto);
    }

    public FreelancerDto getFreelancerProfile(Long id) {

        // 조회수 업데이트
        // 프리랜서 프로필 조회

        return null;
    }


}
