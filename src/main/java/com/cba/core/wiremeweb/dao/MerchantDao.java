package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.MerchantResponseDto;
import org.springframework.data.domain.Page;

public interface MerchantDao<T, K> extends GenericDao<T, K> {
    public Page<MerchantResponseDto> findMerchantsByPartner(int id, int page, int pageSize) throws Exception;
}
