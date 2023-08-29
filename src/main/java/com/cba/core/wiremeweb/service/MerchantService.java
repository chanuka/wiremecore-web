package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.MerchantResponseDto;
import org.springframework.data.domain.Page;

public interface MerchantService<T, K> extends GenericService<T, K> {
    public Page<MerchantResponseDto> findMerchantsByPartner(int id, int page, int pageSize) throws Exception;
}
