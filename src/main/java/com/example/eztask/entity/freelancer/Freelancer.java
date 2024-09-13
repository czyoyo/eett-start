package com.example.eztask.entity.freelancer;

import com.example.eztask.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "freelancer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Comment("프리랜서")
public class Freelancer extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED")
    @Comment("인덱스")
    private long id;

    @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false, unique = true)
    @Comment("이름")
    private String name;

    @Column(name = "detail_view_count", columnDefinition = "BIGINT UNSIGNED", nullable = false)
    @Comment("상세 조회 수")
    @Builder.Default
    private long detailViewCount = 0;


}
