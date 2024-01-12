package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.MerchantResponseDto;
import com.cba.core.wiremeweb.model.Merchant;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface MerchantDao<T, K> extends GenericDao<T, K> {

    Page<Merchant> findMerchantsByPartner(int id, int page, int pageSize) throws Exception;

    Merchant findByMerchantId(String merchantId);

}
