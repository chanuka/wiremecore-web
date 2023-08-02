package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionRepository extends CrudRepository<Permission, Integer> {
}
