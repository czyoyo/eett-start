package com.example.eztask.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.eztask.dto.freelancer.FreelancerDto;
import com.example.eztask.entity.freelancer.Freelancer;
import com.example.eztask.repository.freelancer.FreelancerRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
class FreelancerServiceTest {

    @Mock
    private FreelancerRepository freelancerRepository;

    @InjectMocks
    private FreelancerService freelancerService;


    @Test
    @DisplayName("프리랜서 리스트 조회 단위 테스트")
    void getFreelancerProfileList() {

        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.DESC, "name"));

        // Mock 데이터 생성
        Freelancer freelancer1 = Freelancer.builder().name("a_test").build();
        Freelancer freelancer2 = Freelancer.builder().name("b_test").build();
        List<Freelancer> freelancerList = List.of(freelancer1, freelancer2);
        Page<Freelancer> freelancerPage = new PageImpl<>(freelancerList, pageable, freelancerList.size());

        // Mockito 설정
        Mockito.when(freelancerRepository.findAllFreelancerList(pageable)).thenReturn(freelancerPage);

        // when
        Page<FreelancerDto> freelancerProfileList = freelancerService.getFreelancerProfileList(pageable);

        // then
        assertNotNull(freelancerProfileList, "프리랜서 리스트가 존재하지 않습니다.");
        assertEquals(2, freelancerProfileList.getTotalElements(), "프리랜서 총 개수가 다릅니다.");
        assertEquals(2, freelancerProfileList.getContent().size(), "프리랜서 리스트 크기가 다릅니다.");
    }

    @Test
    void getFreelancerProfile() {
    }
}
