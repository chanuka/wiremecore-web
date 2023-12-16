package com.cba.core.wiremeweb.repository.specification;

import com.cba.core.wiremeweb.model.Device;
import com.cba.core.wiremeweb.model.Merchant;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.model.Terminal;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public interface TerminalSpecification {

    static Specification<Terminal> terminalIdLikeAndMerchantIdLike(String terminalId, String merchantId,String merchantName,String serialNo,String status) throws Exception {
        return (root, query, criteriaBuilder) -> {
            Join<Terminal, Merchant> merchantJoin = root.join("merchant", JoinType.INNER);
            Join<Terminal, Device> deviceJoin = root.join("device", JoinType.INNER);
            Join<Terminal, Status> statusJoin = root.join("status", JoinType.INNER);
            return criteriaBuilder.and(
//                    criteriaBuilder.like(criteriaBuilder.concat("", merchantJoin.get("id")), "%" + merchantId + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(merchantJoin.get("merchantId")), "%" + merchantId.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(merchantJoin.get("name")), "%" + merchantName.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(deviceJoin.get("serialNo")), "%" + serialNo.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(statusJoin.get("statusCode")), "%" + status.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("terminalId")), "%" + terminalId.toLowerCase() + "%")
            );
        };
    }
}
