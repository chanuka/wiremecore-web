package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.DistrictDto;

import java.util.List;
import java.util.Map;

public interface GeneralDao {

    Map<String, List<DistrictDto>> findAllDistrict() throws Exception;

}
