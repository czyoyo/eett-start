package com.example.eztask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.eztask.dto.freelancer.FreelancerDto;
import com.example.eztask.entity.freelancer.Freelancer;
import com.example.eztask.repository.freelancer.FreelancerRepository;
import com.example.eztask.util.ViewCountUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.redis.core.RedisTemplate;

@ExtendWith(MockitoExtension.class)
class FreelancerServiceTest {

    private static final Logger log = LoggerFactory.getLogger(FreelancerServiceTest.class);
    @Mock
    private FreelancerRepository freelancerRepository;

    @InjectMocks
    private FreelancerService freelancerService;

    private List<Freelancer> testFreelancerList;
    private Pageable pageable;

    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ViewCountUtil viewCountUtil;

    @BeforeEach
    void setUp() {
        this.testFreelancerList = createTestFreelancerList();
        pageable = PageRequest.of(0, 10, Sort.by(Direction.DESC, "name"));
    }

    private List<Freelancer> createTestFreelancerList() {
        List<Freelancer> freelancers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            freelancers.add(Freelancer.builder()
                .id((long) i + 1)
                .name("Freelancer " + (char)('A' + i))
                .detailViewCount((long) (i * 10))
                .build());
        }
        return freelancers;
    }


    @Test
    @DisplayName("프리랜서 리스트 조회 단위 테스트")
    void getFreelancerProfileList() {

        // Mock 데이터 생성
        Page<Freelancer> freelancerPage = new PageImpl<>(testFreelancerList, pageable, testFreelancerList.size());

        // Mockito 설정
        Mockito.when(freelancerRepository.findAllFreelancerList(pageable)).thenReturn(freelancerPage);

        // when
        Page<FreelancerDto> result = freelancerService.getFreelancerProfileList(pageable);

        // then
        assertNotNull(result, "프리랜서 리스트가 존재하지 않습니다.");
        assertEquals(5, result.getTotalElements(), "프리랜서 총 개수가 다릅니다.");
        assertEquals(5, result.getContent().size(), "프리랜서 리스트 크기가 다릅니다.");
    }

    @Test
    @DisplayName("프리랜서 단일 프로필 조회 단위 테스트, 조회수 증가 호출 여부 확인")
    void getFreelancerProfile() {
        // given
        Freelancer freelancer = testFreelancerList.get(0);
        log.info("freelancer: {}", freelancer.getName());
        log.info("freelancer: {}", freelancer.getId());
        when(freelancerRepository.findById(freelancer.getId())).thenReturn(Optional.of(freelancer));
        // when
        FreelancerDto freelancerDto = freelancerService.getFreelancerProfile(freelancer.getId());
        log.info("freelancerDto: {}", freelancerDto.getName());
        // then
        assertNotNull(freelancer, "프리랜서가 존재하지 않습니다.");
        assertEquals(freelancer.getName(), freelancerDto.getName(), "프리랜서 이름이 일치하지 않습니다.");
        // 조회수 증가 정확히 한번 호출 되었 는지 확인
        verify(viewCountUtil).incrementViewCount(freelancer.getId());
    }










}
