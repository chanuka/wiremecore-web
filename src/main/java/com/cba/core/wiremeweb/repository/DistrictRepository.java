package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, String>, JpaSpecificationExecutor<District> {
}
