package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.User;

public interface UserDao<T> extends GenericDao<T> {

    User findByUserName(String userName) throws Exception;

}
