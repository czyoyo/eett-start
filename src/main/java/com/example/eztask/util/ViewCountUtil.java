package com.example.eztask.util;

import com.example.eztask.repository.freelancer.FreelancerRepository;
import jakarta.annotation.PreDestroy;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ViewCountUtil {


    // RedisTemplate
    private final RedisTemplate<String, String> redisTemplate;
    private final FreelancerRepository freelancerRepository;


    // 메모리에 저장할 Map
    // concurrentHashMap, AtomicLong 사용 : 멀티스레드 환경에서 안전하게 사용
    // @Component 싱글톤 빈으로 등록되어 하나의 인스턴스만 생성되어 사용
    private final Map<Long, AtomicLong> localCounterMap = new ConcurrentHashMap<>();

    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // 스레드 풀 생성

    // 조회수 증가 (회원 조회 시 호출됨)
    public void incrementViewCount(Long freelancerId) {
        // computeIfAbsent : key 가 없으면 새로운 값을 추가하고, 있으면 +1 을 해준다.
        localCounterMap.computeIfAbsent(freelancerId, k -> new AtomicLong()).incrementAndGet();
    }

    // 로컬 카운터를 Redis 로 flush (스케줄러 호출)
    public void flushLocalCountersToRedis() {

        // 메모리 데이터를 복사
        Map<Long, AtomicLong> currentCounts = new ConcurrentHashMap<>(localCounterMap);

        // 복사 후 메모리 데이터 초기화 (비우고 새로운 데이터를 받기 위함)
        localCounterMap.clear();

        // Redis 에 저장
        for (Map.Entry<Long, AtomicLong> entry : currentCounts.entrySet()) {
            // Redis 키 : freelancer:view:{freelancerId}
            String key = "freelancer:view:" + entry.getKey();

            executorService.submit(() -> {
                redisTemplate.opsForValue().increment(key, entry.getValue().get());
            });
            // Redis 의 해당 키 값을 조회수 만큼 증가
            // increment : 해당 키가 없으면 생성, 없으면 추가 (증가)

        }

    }

    // 조회수를 DB 에 저장 (스케줄러 호출)
    public void flushRedisCountersToDB() {
        Set<String> keys = redisTemplate.keys("freelancer:view:*");
        for (String key : keys) {
            executorService.submit(() -> {
                String countStr = redisTemplate.opsForValue().getAndDelete(key);
                if (countStr != null) {
                    Long freelancerId = Long.parseLong(key.split(":")[2]);
                    Long count = Long.parseLong(countStr);
                    freelancerRepository.increaseViewCount(freelancerId, count);
                    log.info("flushRedisCountersToDB flush key: {}, count: {}", key, count);
                }
            });
        }
    }

    // 스프링 종료 시 스레드 풀 종료 (작업중 스레드 작업후 종료)
    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("Error while shutting down executor", e);
            executorService.shutdownNow();
        }
    }






}


