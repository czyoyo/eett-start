package com.example.eztask.repository.freelancer;

import com.example.eztask.entity.freelancer.Freelancer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FreelancerRepositoryCustomRepository {

    // 프리랜서 목록 조회 (페이징 처리, sort)
    Page<Freelancer> findAllFreelancerList(Pageable pageable);

}
