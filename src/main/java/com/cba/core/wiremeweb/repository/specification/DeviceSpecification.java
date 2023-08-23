package com.cba.core.wiremeweb.repository.specification;

import com.cba.core.wiremeweb.model.Device;
import org.springframework.data.jpa.domain.Specification;

public interface DeviceSpecification {

    static Specification<Device> serialNoLikeAndDeviceTypeLike(String serialNo,String deviceType) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("serialNo")), "%" + serialNo.toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("deviceType")), "%" + deviceType.toLowerCase() + "%")
                );
    }
}
