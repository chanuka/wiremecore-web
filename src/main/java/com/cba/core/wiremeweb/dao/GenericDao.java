package com.cba.core.wiremeweb.dao;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface GenericDao<T> {

    Page<T> findAll(int page, int pageSize) throws Exception;

    List<T> findAll() throws Exception;

    Page<T> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception;

    Page<T> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter, int page, int pageSize) throws Exception;

    T findById(int id) throws Exception;

    void deleteById(int id) throws Exception;

    void deleteByIdList(List<Integer> idList) throws Exception;

    T updateById(int id, T requestDto) throws Exception;

    T create(T requestDto) throws Exception;

    List<T> createBulk(List<T> requestDtoList) throws Exception;
}
