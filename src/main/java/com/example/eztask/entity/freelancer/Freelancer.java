package com.example.eztask.entity.freelancer;

import com.example.eztask.entity.BaseEntity;
import com.example.eztask.entity.payment.PaymentData;
import com.example.eztask.entity.payment.PrePaymentData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "freelancer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Comment("프리랜서")
public class Freelancer extends BaseEntity {


    private static final Logger log = LoggerFactory.getLogger(Freelancer.class);
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

    @Column(name = "point", columnDefinition = "INT", nullable = false)
    @Comment("포인트")
    @Builder.Default
    private BigDecimal point = BigDecimal.ZERO;

    @OneToMany(mappedBy = "freelancer")
    @Comment("사전 결제 데이터")
    private List<PrePaymentData> prePaymentDataList = new ArrayList<>();

    @OneToMany(mappedBy = "freelancer")
    @Comment("결제 데이터")
    private List<PaymentData> paymentList = new ArrayList<>();


    public void addPoint(BigDecimal point) {
        this.point = this.point.add(point);
    }

}
