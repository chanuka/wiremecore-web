package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUserName(String username);
}
