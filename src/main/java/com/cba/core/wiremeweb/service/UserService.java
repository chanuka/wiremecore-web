package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.ChangePasswordRequestDto;

public interface UserService<T, K> extends GenericService<T, K> {

    String changePassword(ChangePasswordRequestDto requestDto) throws Exception;

    String accountLockReset(String userName) throws Exception;

}
