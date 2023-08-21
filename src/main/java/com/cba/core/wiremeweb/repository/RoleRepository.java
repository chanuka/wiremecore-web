package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Page<Role> findByRoleNameLike(String serialNo, Pageable pageable);

}
