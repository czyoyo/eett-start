package com.example.eztask.repository.freelancer;

import static org.junit.jupiter.api.Assertions.*;

import com.example.eztask.config.QueryDslConfig;
import com.example.eztask.entity.freelancer.Freelancer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig.class)
class FreelancerRepositoryImplTest {

    @Autowired
    private FreelancerRepository freelancerRepository;

    @Test
    @DisplayName("findAllFreelancerList 테스트")
    void findAllFreelancerList() {

        // given
        // 기존 데이터 갯수 확인
        long count = freelancerRepository.count();

        // 10개 추가
        for (int i = 0; i < 15; i++) {
            Freelancer freelancer = Freelancer.builder()
                .name("test" + i)
                .build();
            freelancerRepository.save(freelancer);
        }

        Pageable pageRequest = PageRequest.of(0, 10, Sort.by(Direction.ASC, "name"));


        // when
        Page<Freelancer> freelancerPage
            = freelancerRepository.findAllFreelancerList(pageRequest);

        // then
        assertNotNull(freelancerPage, "프리랜서 리스트가 존재하지 않습니다.");
        assertTrue(freelancerPage.getTotalElements() >= count + 15, "프리랜서 총 개수가 다릅니다.");
    }





}
