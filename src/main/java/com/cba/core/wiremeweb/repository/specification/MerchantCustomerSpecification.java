package com.cba.core.wiremeweb.repository.specification;

import com.cba.core.wiremeweb.model.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public interface MerchantCustomerSpecification {

    static Specification<MerchantCustomer> nameLikeAndStatusLike(String merchantCustomerName, String statusCode, String address) throws Exception {
        return (root, query, criteriaBuilder) -> {
            Join<MerchantCustomer, Status> join = root.join("status", JoinType.INNER);
            return criteriaBuilder.and(
                    criteriaBuilder.like(criteriaBuilder.lower(join.get("statusCode")), "%" + statusCode.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + merchantCustomerName.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%" + address.toLowerCase() + "%")

            );
        };
    }

    static Specification<MerchantCustomer> allLike(String keyWord) throws Exception {
        return (root, query, criteriaBuilder) -> {
            Join<MerchantCustomer, Status> joinStatus = root.join("status", JoinType.INNER);
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(joinStatus.get("statusCode")), "%" + keyWord.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyWord.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%" + keyWord.toLowerCase() + "%")
            );
        };
    }
}
