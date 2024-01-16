package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.HighlightRequestDto;
import com.cba.core.wiremeweb.model.TransactionCore;
import com.cba.core.wiremeweb.model.UserConfig;

import java.util.List;

public interface HighlightDao {

    List<UserConfig> findAll(String userName, String configType) throws Exception;

    UserConfig findByUser_NameAndConfigName(String userName, String configName) throws Exception;

    void deleteByUser_NameAndConfigName(String userName,String configName) throws Exception;

    UserConfig create(UserConfig requestDto) throws Exception;

    UserConfig update(String configName, UserConfig requestDto) throws Exception;

    List<Object[]> findHighLights(String whereClause, String selectClause, String groupByClause,
                                                    HighlightRequestDto requestDto) throws Exception;

    List<TransactionCore> findHighLightsDetail(String whereClause,HighlightRequestDto requestDto) throws Exception;


}
