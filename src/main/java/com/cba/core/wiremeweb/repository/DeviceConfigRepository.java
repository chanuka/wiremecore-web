package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.DeviceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface DeviceConfigRepository extends JpaRepository<DeviceConfig, Integer>, JpaSpecificationExecutor<DeviceConfig> {

    Optional<DeviceConfig> findByDevice_Id(int deviceId);

    void deleteByDevice_Id(int deviceId);

}
