package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.DeviceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeviceModelRepository extends JpaRepository<DeviceModel, Integer>, JpaSpecificationExecutor<DeviceModel> {
}
