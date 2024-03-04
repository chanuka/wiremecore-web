package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.Mcc;

public interface MccDao {

    Mcc findByCode(String code) throws Exception;
}
