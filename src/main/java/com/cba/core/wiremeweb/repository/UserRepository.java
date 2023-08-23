package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.User;
import com.cba.core.wiremeweb.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    User findByUserName(String userName);

    User findByUserNameAndUserType(String userName, UserType userType);

}
