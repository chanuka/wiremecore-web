package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.Permission;
import com.cba.core.wiremeweb.model.Role;
import jakarta.persistence.NamedQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionRepository extends CrudRepository<Permission, Integer> {

    @Query("SELECT p FROM Permission p WHERE p.role = (SELECT ur.role from UserRole ur WHERE ur.userByUserId=(SELECT u FROM User u WHERE u.userName=:username))")
    Iterable<Permission> findAllByRole(String username);

}
