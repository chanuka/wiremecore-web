package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.User;
import com.cba.core.wiremeweb.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserNameAndUserType(String userName, UserType userType);

}
