package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Integer>, JpaSpecificationExecutor<Device> {

    List<Device> findByIsAway(boolean isAway);
}
