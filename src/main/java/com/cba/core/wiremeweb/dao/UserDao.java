package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.User;

public interface UserDao<T> extends GenericDao<T> {

    User changePassword(User requestDto) throws Exception;

    User findByUserName(String userName) throws Exception;

}
