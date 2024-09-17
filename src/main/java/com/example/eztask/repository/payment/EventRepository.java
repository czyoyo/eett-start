package com.example.eztask.repository.payment;

import com.example.eztask.entity.payment.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByEventCode(String eventCode);
}
