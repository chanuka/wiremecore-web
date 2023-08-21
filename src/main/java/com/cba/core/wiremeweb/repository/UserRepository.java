package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.User;
import com.cba.core.wiremeweb.model.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByUserName(String userName);

    User findByUserNameAndUserType(String userName, UserType userType);

    Page<User> findByUserNameLike(String serialNo, Pageable pageable);

}
