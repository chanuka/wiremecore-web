package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.ChangePasswordRequestDto;
import com.cba.core.wiremeweb.model.User;

public interface UserDao<T, K> extends GenericDao<T, K> {

    String changePassword(ChangePasswordRequestDto requestDto, String userName) throws Exception;

    String accountLockReset(String userName) throws Exception;

    User findByUserName(String userName) throws Exception;

}
