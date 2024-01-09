package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer>, JpaSpecificationExecutor<Device> {

    List<Device> findByIsAway(boolean isAway);
}
