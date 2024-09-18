package com.example.eztask.scheduler;

import com.example.eztask.util.ViewCountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ViewCountScheduler {

    private final ViewCountUtil viewCountUtil;

    // 5초마다 로컬 카운터를 Redis 로 flush
    @Scheduled(fixedRate = 60000) // 1분
    public void flushLocalCountersToRedis() {
        log.info("flushLocalCountersToRedis start");
        viewCountUtil.flushLocalCountersToRedis();
        log.info("flushLocalCountersToRedis end");
    }

    // 5초마다 Redis 에서 조회수를 DB 에 저장
    @Scheduled(fixedRate = 300000) // 5분
    public void flushRedisCountersToDB() {
        log.info("flushRedisCountersToDB start");
        viewCountUtil.flushRedisCountersToDB();
        log.info("flushRedisCountersToDB end");
    }

}
