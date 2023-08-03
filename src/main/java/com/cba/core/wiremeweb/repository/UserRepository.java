package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.User;
import com.cba.core.wiremeweb.model.UserType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUserName(String userName);
    User findByUserNameAndUserType(String userName, UserType userType);
}
