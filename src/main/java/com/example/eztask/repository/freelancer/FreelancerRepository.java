package com.example.eztask.repository.freelancer;

import com.example.eztask.entity.freelancer.Freelancer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreelancerRepository extends JpaRepository<Freelancer, Long>, FreelancerRepositoryCustomRepository {

    Optional<Freelancer> findByName(String name);

}
