package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ResourceRepository extends JpaRepository<Resource, Integer> {
}
