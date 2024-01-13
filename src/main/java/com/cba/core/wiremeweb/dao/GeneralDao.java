package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.DistrictDto;
import com.cba.core.wiremeweb.model.District;

import java.util.List;
import java.util.Map;

public interface GeneralDao {

    List<District> findAllDistrict() throws Exception;

}
