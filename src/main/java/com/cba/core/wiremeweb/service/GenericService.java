package com.cba.core.wiremeweb.service;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface GenericService<T, K> {

    public Page<T> findAll(int page, int pageSize) throws Exception;

    public List<T> findAll() throws Exception;

    public Page<T> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception;

    public T findById(int id) throws Exception;

    public T deleteById(int id) throws Exception;

    public void deleteByIdList(List<Integer> idList) throws Exception;

    public T updateById(int id, K requestDto) throws Exception;

    public T create(K requestDto) throws Exception;

    public List<T> createBulk(List<K> requestDtoList) throws Exception;

    public byte[] exportPdfReport() throws Exception;

    public byte[] exportExcelReport() throws Exception;
}
