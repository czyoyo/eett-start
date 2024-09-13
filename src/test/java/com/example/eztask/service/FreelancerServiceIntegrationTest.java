package com.example.eztask.service;

import com.example.eztask.dto.freelancer.FreelancerDto;
import com.example.eztask.entity.freelancer.Freelancer;
import com.example.eztask.repository.freelancer.FreelancerRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional // rollback by default
public class FreelancerServiceIntegrationTest {

    @Autowired
    private FreelancerService freelancerService;

    @Autowired
    private FreelancerRepository freelancerRepository;

    @Test
    void testGetFreelancerProfileList() {

        // 기존 데이터 갯수 확인
        long count = freelancerRepository.count();

        // given
        givenFreelancerProfileList();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "created_at"));

        // when
        Page<FreelancerDto> freelancerProfileList = freelancerService.getFreelancerProfileList(pageable);

        assertNotNull(freelancerProfileList, "프리랜서 리스트가 존재하지 않습니다.");
        // 기존 데이터 갯수 + 10개
        assertEquals(count + 10, freelancerProfileList.getTotalElements(), "프리랜서 총 개수가 다릅니다.");
    }

    private void givenFreelancerProfileList() {
        // given
        for (int i = 0; i < 10; i++) {
            // save
            Freelancer freelancer = Freelancer.builder()
                .name("freelancer" + i)
                .build();
            freelancerRepository.save(freelancer);
        }
    }



}
