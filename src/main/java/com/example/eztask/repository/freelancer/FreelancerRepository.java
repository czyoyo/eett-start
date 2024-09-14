package com.example.eztask.repository.freelancer;

import com.example.eztask.entity.freelancer.Freelancer;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FreelancerRepository extends JpaRepository<Freelancer, Long>, FreelancerRepositoryCustomRepository {

    Optional<Freelancer> findByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Freelancer f SET f.detailViewCount = f.detailViewCount + :count WHERE f.id = :id")
    void increaseViewCount(@Param("id") Long id ,@Param("count") Long count);

    // 가장 최근에 등록된 프리랜서 한명 조회
    @Query("SELECT f FROM Freelancer f ORDER BY f.id DESC")
    Optional<Freelancer> findRecentFreelancer();

}
