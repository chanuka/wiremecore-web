package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.GraphRequestDto;
import com.cba.core.wiremeweb.dto.GraphResponseDto;
import com.cba.core.wiremeweb.model.UserConfig;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GraphDao {

    List<UserConfig> findAll(String userName, String configType) throws Exception;

    UserConfig findByUser_NameAndConfigName(String userName, String configName) throws Exception;

    UserConfig deleteByUser_NameAndConfigType(String userName, String configType) throws Exception;

    UserConfig create(UserConfig requestDto) throws Exception;

    UserConfig update(String configName, UserConfig requestDto) throws Exception;

    List<Object[]> findGraphs(String whereClause,
                              String selectClause,
                              String groupByClause,
                              GraphRequestDto requestDtoGraph) throws Exception;

}
