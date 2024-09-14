package com.example.eztask.service;

import com.example.eztask.dto.freelancer.FreelancerDto;
import com.example.eztask.entity.freelancer.Freelancer;
import com.example.eztask.repository.freelancer.FreelancerRepository;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

import com.example.eztask.util.ViewCountUtil;
import jakarta.persistence.EntityManager;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional // rollback by default
public class FreelancerServiceIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(
        FreelancerServiceIntegrationTest.class);
    @Autowired
    private FreelancerService freelancerService;

    @Autowired
    private FreelancerRepository freelancerRepository;

    @Autowired
    private ViewCountUtil viewCountUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private EntityManager entityManager;

    private long initialCount;

    // 테스트 데이터 갯수
    private int testCount = 5;

    @BeforeEach
    void setUp() {
        initialCount = freelancerRepository.count();
        givenFreelancerProfileList();
    }

    @Test
    @DisplayName("프리랜서 리스트 조회 테스트")
    void testGetFreelancerProfileList() {

        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "created_at"));

        // when
        Page<FreelancerDto> freelancerProfileList = freelancerService.getFreelancerProfileList(pageable);

        assertNotNull(freelancerProfileList, "프리랜서 리스트가 존재하지 않습니다.");
        // 기존 데이터 갯수 + 10개
        assertEquals(initialCount + testCount, freelancerProfileList.getTotalElements(), "프리랜서 총 개수가 다릅니다.");
    }

    @Test
    @DisplayName("프리랜서 조회 테스트")
    void testGetFreelancerProfile() {
        // given
        Freelancer freelancer = freelancerRepository.findAll().get(0);
        FreelancerDto freelancerDto = freelancerService.getFreelancerProfile(freelancer.getId());


        assertNotNull(freelancerDto, "프리랜서 정보가 존재하지 않습니다.");
        assertEquals(freelancer.getName(), freelancerDto.getName(), "프리랜서 이름이 다릅니다.");

    }



    @Test
    @DisplayName("존재하지 않는 프리랜서 조회 테스트")
    void testGetFreelancerProfileNotExist() {
        // given
        Long notExistFreelancerId = 99999999L;
        assertThrows(NoSuchElementException.class, () -> {
            // when
            freelancerService.getFreelancerProfile(notExistFreelancerId);
        });
    }




    private void givenFreelancerProfileList() {
        // given
        for (int i = 0; i < testCount; i++) {
            // save
            Freelancer freelancer = Freelancer.builder()
                .name("freelancer" + i)
                .build();
            freelancerRepository.save(freelancer);
            log.info("프리랜서 생성 : {}", freelancer);
        }
    }





}
