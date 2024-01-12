package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.DeviceVendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface DeviceVendorRepository extends JpaRepository<DeviceVendor, Integer>, JpaSpecificationExecutor<DeviceVendor> {
}
