package com.example.eztask.repository.freelancer;

import static org.junit.jupiter.api.Assertions.*;

import com.example.eztask.config.QueryDslConfig;
import com.example.eztask.entity.freelancer.Freelancer;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig.class)
class FreelancerRepositoryTest {

    @Autowired
    private FreelancerRepository freelancerRepository;


    @Test
    @DisplayName("findByName 테스트")
    void findByName() {
        // given
        String name = "test";

        Freelancer freelancer = Freelancer.builder()
            .name(name)
            .build();

        freelancerRepository.save(freelancer);

        // when
        Optional<Freelancer> findFreelancer = freelancerRepository.findByName(name);

        // then
        assertNotNull(findFreelancer, "프리랜서가 존재하지 않습니다.");
        assertTrue(findFreelancer.isPresent(), "프리랜서가 존재하지 않습니다.");
        assertEquals(findFreelancer.get().getName(), name, "프리랜서 이름이 일치하지 않습니다.");
        assertEquals(findFreelancer.get().getId(), freelancer.getId(), "프리랜서 ID가 일치하지 않습니다.");
        assertEquals(findFreelancer.get().getCreatedAt(), freelancer.getCreatedAt(), "프리랜서 생성일이 일치하지 않습니다.");
    }




}
