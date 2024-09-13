package com.example.eztask;

import com.example.eztask.common.CommonResponse;
import com.example.eztask.entity.freelancer.Freelancer;
import com.example.eztask.repository.freelancer.FreelancerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // real server
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // for @BeforeAll
@Transactional
public class FreelancerE2ETest {

    Logger log = LoggerFactory.getLogger(FreelancerE2ETest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FreelancerRepository freelancerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullFreeLancerTest() throws JsonProcessingException {

        // 프리랜서 생성 API 는 없으므로 수동으로 생성
        for(int i = 0; i < 10; i++) {
            Freelancer freelancer = Freelancer.builder()
                .name("freelancer" + i)
                .build();

            freelancerRepository.save(freelancer);
        }

        String url = UriComponentsBuilder.fromPath("/api/freelancer")
            .queryParam("page", 0)
            .queryParam("size", 10)
            .queryParam("sort", "name,desc")
            .toUriString();

        // 프리랜서 목록 조회
        ResponseEntity<CommonResponse> response = restTemplate.getForEntity(
            url,
            CommonResponse.class
        );

        // 응답 검증

        String responseJson = objectMapper.writeValueAsString(response.getBody());
        log.info("Response JSON: {}", responseJson);

        //:TODO 프리랜서 목록 조회 이후 검증 추가 필요







    }





}
