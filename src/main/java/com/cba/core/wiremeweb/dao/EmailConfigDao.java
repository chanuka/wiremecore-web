package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.EmailConfig;

public interface EmailConfigDao {

    EmailConfig findByAction(String action) throws Exception;
}
