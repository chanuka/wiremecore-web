package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.District;

import java.util.List;

public interface GeneralDao {

    List<District> findAllDistrict() throws Exception;

}
