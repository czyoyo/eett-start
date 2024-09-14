package com.example.eztask.service;

import com.example.eztask.dto.freelancer.FreelancerDto;
import com.example.eztask.entity.freelancer.Freelancer;
import com.example.eztask.repository.freelancer.FreelancerRepository;
import com.example.eztask.util.ViewCountUtil;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FreelancerService {

    private final FreelancerRepository freelancerRepository;
    private final ViewCountUtil viewCountUtil;


    @Transactional(readOnly = true)
    public Page<FreelancerDto> getFreelancerProfileList(Pageable pageable) {
        // pageable 의 size 가 10 초과인 경우 예외처리
        if (pageable.getPageSize() > 10) {
            throw new IllegalArgumentException("페이지 사이즈는 10 이하로 설정해주세요.");
        }
        Page<Freelancer> allFreelancerList = freelancerRepository.findAllFreelancerList(pageable);
        return allFreelancerList.map(FreelancerDto::convertToDto);
    }

    @Transactional
    public FreelancerDto getFreelancerProfile(Long id) {

        Freelancer freelancer = freelancerRepository.findById(id).orElseThrow(() -> new NoSuchElementException("프리랜서 정보가 존재하지 않습니다."));

        // 조회수 증가
        viewCountUtil.incrementViewCount(id);

        return FreelancerDto.convertToDto(freelancer);
    }

    // 프리랜서 생성
    @Transactional
    public Long createFreelancer() {

        // 랜덤 프리랜서 생성
        Freelancer freelancer = Freelancer.builder()
            .name("freelancer_" + (int) (Math.random() * 1000))
            .detailViewCount(0L)
            .build();

        return freelancerRepository.save(freelancer).getId();

    }


}
