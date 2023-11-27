package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.DistrictDto;

import java.util.List;
import java.util.Map;

public interface GeneralService {

    Map<String, List<DistrictDto>> findAllDistrict() throws Exception;

}
