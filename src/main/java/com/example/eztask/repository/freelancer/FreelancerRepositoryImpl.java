package com.example.eztask.repository.freelancer;

import com.example.eztask.entity.freelancer.Freelancer;
import com.example.eztask.entity.freelancer.QFreelancer;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class FreelancerRepositoryImpl implements FreelancerRepositoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Freelancer> findAllFreelancerList(Pageable pageable) {

        List<Freelancer> freelancerList = queryFactory
            .selectFrom(QFreelancer.freelancer)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getOrderSpecifiers(pageable))
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(QFreelancer.freelancer.count())
            .from(QFreelancer.freelancer);

        return PageableExecutionUtils.getPage(freelancerList, pageable, countQuery::fetchOne);
    }

    @Override
    public Freelancer findFreelancerProfile(Long id) {
        return null;
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        return pageable.getSort().isUnsorted() ? new OrderSpecifier[]{QFreelancer.freelancer.createdAt.desc()} :
            pageable.getSort().stream()
                .map(order -> {
                    if (order.isAscending()) {
                        return switch (order.getProperty()) {
                            case "created_at" -> QFreelancer.freelancer.createdAt.asc();
                            case "name" -> QFreelancer.freelancer.name.asc();
                            case "detail_view_count" -> QFreelancer.freelancer.detailViewCount.asc();
                            default -> null;
                        };
                    } else {
                        return switch (order.getProperty()) {
                            case "created_at" -> QFreelancer.freelancer.createdAt.desc();
                            case "name" -> QFreelancer.freelancer.name.desc();
                            case "detail_view_count" -> QFreelancer.freelancer.detailViewCount.desc();
                            default -> null;
                        };
                    }
                })
                .toArray(OrderSpecifier[]::new);
    }


}
