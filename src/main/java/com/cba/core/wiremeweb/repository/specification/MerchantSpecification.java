package com.cba.core.wiremeweb.repository.specification;

import com.cba.core.wiremeweb.model.Merchant;
import com.cba.core.wiremeweb.model.Status;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public interface MerchantSpecification {

    static Specification<Merchant> nameLikeAndStatusLike(String merchantName, String statusCode) throws Exception {
        return (root, query, criteriaBuilder) -> {
            Join<Merchant, Status> join = root.join("status", JoinType.INNER);
            return criteriaBuilder.and(
                    criteriaBuilder.like(criteriaBuilder.lower(join.get("statusCode")), "%" + statusCode.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + merchantName.toLowerCase() + "%")
            );
        };
    }
}
