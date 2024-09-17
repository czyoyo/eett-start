package com.example.eztask.entity.payment;


import com.example.eztask.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event", indexes = {
    @Index(name = "idx_event_code", columnList = "event_code")
})
public class Event extends BaseEntity { // 이벤트 코드에 따라 포인트 추가적립

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED")
    @Comment("인덱스")
    private long id;

    @Column(name = "event_code", columnDefinition = "VARCHAR(50)", nullable = false)
    @Comment("이벤트 코드")
    private String eventCode;

    // 포인트 추가적립 비율
    @Column(name = "point_rate", columnDefinition = "INT", nullable = false)
    @Comment("포인트 비율")
    private int pointRate;

    // 이벤트 설명
    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    @Comment("이벤트 설명")
    private String description;

    // 이벤트 이름
    @Column(name = "name", columnDefinition = "VARCHAR(50)", nullable = false)
    @Comment("이벤트 이름")
    private String name;

    // 이벤트 시작일
    @Column(name = "start_date", columnDefinition = "DATETIME", nullable = false)
    @Comment("이벤트 시작일")
    private LocalDateTime startDate;

    // 이벤트 종료일
    @Column(name = "end_date", columnDefinition = "DATETIME", nullable = false)
    @Comment("이벤트 종료일")
    private LocalDateTime endDate;

}
