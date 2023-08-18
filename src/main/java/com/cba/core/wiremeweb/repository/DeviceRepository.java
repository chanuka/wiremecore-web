package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device,Integer> {

    Page<Device> findBySerialNoLike(String serialNo, Pageable pageable);

}
