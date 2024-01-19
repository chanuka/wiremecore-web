package com.cba.core.wiremeweb.repository.specification;

import com.cba.core.wiremeweb.model.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public interface DeviceSpecification {

    static Specification<Device> serialNoLikeAndDeviceTypeLike(String serialNo, String deviceType, String statusCode,
                                                               String deviceModel, String deviceVendor) throws Exception {
        return (root, query, criteriaBuilder) -> {
            Join<Device, DeviceModel> joinModel = root.join("deviceModel", JoinType.INNER);
            Join<Device, Status> joinStatus = root.join("status", JoinType.INNER);
            Join<DeviceModel, DeviceVendor> joinVendor = joinModel.join("deviceVendor", JoinType.INNER);
            return criteriaBuilder.and(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("serialNo")), "%" + serialNo.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("deviceType")), "%" + deviceType.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(joinStatus.get("statusCode")), "%" + statusCode.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(joinModel.get("name")), "%" + deviceModel.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(joinVendor.get("name")), "%" + deviceVendor.toLowerCase() + "%")

            );
        };
    }
    static Specification<Device> allLike(String keyWord) throws Exception {
        return (root, query, criteriaBuilder) -> {
            Join<Device, Status> joinStatus = root.join("status", JoinType.INNER);
            Join<Device, DeviceModel> joinModel = root.join("deviceModel", JoinType.INNER);
            Join<DeviceModel, DeviceVendor> joinVendor = joinModel.join("deviceVendor", JoinType.INNER);

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(joinStatus.get("statusCode")), "%" + keyWord.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("serialNo")), "%" + keyWord.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("deviceType")), "%" + keyWord.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(joinModel.get("name")), "%" + keyWord.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(joinVendor.get("name")), "%" + keyWord.toLowerCase() + "%")
            );
        };
    }
}
