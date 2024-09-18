package com.example.eztask.scheduler;

import com.example.eztask.util.ViewCountUtil;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ViewCountScheduler {

    private final ViewCountUtil viewCountUtil;
    private final AtomicInteger noPeakCounter = new AtomicInteger(0);
    private static final int FLUSH_INTERVAL = 2; // 2번 호출마다 flush
    private static final int RESET_THRESHOLD = 100; // 100번 호출마다 reset

    // 1분마다 로컬 카운터를 Redis 로 flush
    @Scheduled(fixedRate = 60000) // 1분
    public void flushLocalCountersToRedis() {
        log.info("flushLocalCountersToRedis start");
        viewCountUtil.flushLocalCountersToRedis();
        log.info("flushLocalCountersToRedis end");
    }

    // 5분마다 Redis 에서 조회수를 DB 에 저장
    @Scheduled(fixedRate = 300000) // 5분
    public void flushRedisCountersToDB() {
        log.info("flushRedisCountersToDB start");

        if(isPeakHour()) { // 피크 시간대면 호출 횟수와 상관없이 5분마다 flush
                log.info("flushRedisCountersToDB peak hour start");
                viewCountUtil.flushRedisCountersToDB();
                log.info("flushRedisCountersToDB peak hour end");
        } else { // 피크 시간이 아니면 호출 횟수가 2의 배수(현재)일 때 flush
            int currentCount = noPeakCounter.incrementAndGet();
            if(shouldFlush()) {
                log.info("flushRedisCountersToDB no peak hour start");
                viewCountUtil.flushRedisCountersToDB();
                log.info("flushRedisCountersToDB no peak hour end");
            }
            resetCounter(currentCount);
        }


    }


    // 피크 시간대인지 확인
    private boolean isPeakHour() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(now.with(LocalTime.of(9, 0))) && now.isBefore(now.with(LocalTime.of(18, 0))) // 9시~18시
            && now.getDayOfWeek().getValue() < 6; // 월~금(1~5)
    }

    // 피크시간이 아닐 때 호출 횟수가 2의 배수(현재)일 때 flush 하기 위함
    private boolean shouldFlush() {
        return noPeakCounter.get() % FLUSH_INTERVAL == 0;
    }

    // 카운트를 증가 시키며 100번 호출마다 reset
    private void resetCounter(int currentCount) {
        if(currentCount >= RESET_THRESHOLD) {
            noPeakCounter.set(0);
        }
    }

}
