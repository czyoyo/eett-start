package com.example.eztask.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.example.eztask.config.GlobalExceptionHandler;
import com.example.eztask.dto.freelancer.FreelancerDto;
import com.example.eztask.enums.ResponseCode;
import com.example.eztask.service.FreelancerService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
class FreelancerControllerTest {

    @Mock
    private FreelancerService freelancerService;

    @InjectMocks
    private FreelancerController freelancerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(freelancerController)
            .setControllerAdvice(new GlobalExceptionHandler()) // 전역 예외 핸들러 추가
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()) // Pageable 처리를 위한 설정
            .setMessageConverters(new MappingJackson2HttpMessageConverter()) // JSON 변환을 위한 메시지 컨버터
            .setValidator(validator)
            .build();
    }

    @Test
    @DisplayName("프리랜서 리스트 조회 컨트롤러 단위 테스트")
    void getFreelancerProfileList() throws Exception {

        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.DESC, "name"));
        FreelancerDto freelancerDto = FreelancerDto.builder()
            .id(1L)
            .name("John Doe")
            .detailViewCount(10L)
            .createdAt(LocalDateTime.of(2021, 1, 1, 0, 0))
            .build();
        FreelancerDto freelancerDto2 = FreelancerDto.builder()
            .id(2L)
            .name("Jane Doe")
            .detailViewCount(100L)
            .createdAt(LocalDateTime.of(2021, 1, 2, 0, 0))
            .build();
        List<FreelancerDto> freelancerDtoList = List.of(freelancerDto, freelancerDto2);
        Page<FreelancerDto> freelancerDtoPage = new PageImpl<>(freelancerDtoList, pageable,
            freelancerDtoList.size());

        // Mock 설정
        Mockito.when(freelancerService.getFreelancerProfileList(any(Pageable.class)))
            .thenReturn(freelancerDtoPage);



        // when & then
        mockMvc.perform(
                get("/api/freelancer")
                    .param("page", "0")
                    .param("size", "10")
                    .param("sort", "detail_view_count,asc")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(
                result -> {
                    System.out.println(
                        "result.getResponse().getContentAsString() = " +
                        result.getResponse().getContentAsString()
                    );
                }
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()))
            .andExpect(jsonPath("$.message").value(ResponseCode.SUCCESS.getMessage()))
            .andExpect(jsonPath("$.data.content[0].id").value(1))
            .andExpect(jsonPath("$.data.content[0].name").value("John Doe"))
            .andExpect(jsonPath("$.data.content[0].detailViewCount").value(10))
            .andExpect(jsonPath("$.data.content[1].id").value(2))
            .andExpect(jsonPath("$.data.content[1].name").value("Jane Doe"))
            .andExpect(jsonPath("$.data.content[1].detailViewCount").value(100));
    }
}
