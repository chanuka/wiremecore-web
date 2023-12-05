package com.cba.core.wiremeweb.repository.specification;

import com.cba.core.wiremeweb.model.Merchant;
import com.cba.core.wiremeweb.model.MerchantCustomer;
import com.cba.core.wiremeweb.model.Status;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public interface MerchantSpecification {

    static Specification<Merchant> nameLikeAndStatusLike(String merchantName, String statusCode,String merchantId,String partnerName) throws Exception {
        return (root, query, criteriaBuilder) -> {
            Join<Merchant, Status> joinStatus = root.join("status", JoinType.INNER);
            Join<Merchant, MerchantCustomer> joinPartner = root.join("merchantCustomer", JoinType.INNER);
            return criteriaBuilder.and(
                    criteriaBuilder.like(criteriaBuilder.lower(joinStatus.get("statusCode")), "%" + statusCode.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + merchantName.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("merchantId")), "%" + merchantId.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(joinPartner.get("name")), "%" + partnerName.toLowerCase() + "%")
            );
        };
    }
}
