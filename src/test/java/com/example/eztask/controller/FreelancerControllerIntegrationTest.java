package com.example.eztask.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.eztask.enums.ResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FreelancerControllerIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(
        FreelancerControllerIntegrationTest.class);
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("프리랜서 리스트 조회 컨트롤러 통합 테스트")
    void testGetFreelancerProfileList() throws Exception {

        mockMvc.perform(get("/api/freelancer")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "name,desc")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(
                result -> {
                    log.info(result.getResponse().getContentAsString());
                }
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()))
            .andExpect(jsonPath("$.message").value(ResponseCode.SUCCESS.getMessage()))
            .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    @DisplayName("프리랜서 단일 프로필 조회 컨트롤러 통합 테스트 - 페이지 사이즈 오류")
    void testGetFreelancerProfileListWithInvalidPageSize() throws Exception {

        mockMvc.perform(get("/api/freelancer")
                .param("page", "0")
                .param("size", "99999999")
                .param("sort", "name,desc")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(
                result -> {
                    log.info(result.getResponse().getContentAsString());
                }
            )
            .andExpect(status().isBadRequest());
    }

}
