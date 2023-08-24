package com.cba.core.wiremeweb.repository.specification;

import com.cba.core.wiremeweb.model.Merchant;
import com.cba.core.wiremeweb.model.Terminal;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public interface TerminalSpecification {

    static Specification<Terminal> terminalIdLikeAndMerchantIdLike(String terminalId, String merchantId) throws Exception {
        return (root, query, criteriaBuilder) -> {
            Join<Terminal, Merchant> merchantJoin = root.join("merchant", JoinType.INNER);
            return criteriaBuilder.and(
//                    criteriaBuilder.like(criteriaBuilder.concat("", merchantJoin.get("id")), "%" + merchantId + "%"),
                    criteriaBuilder.equal(merchantJoin.get("id"), Integer.parseInt(merchantId)),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("terminalId")), "%" + terminalId.toLowerCase() + "%")
            );
        };
    }
}
