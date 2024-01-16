package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.Merchant;
import org.springframework.data.domain.Page;

public interface MerchantDao<T> extends GenericDao<T> {

    Page<Merchant> findMerchantsByPartner(int id, int page, int pageSize) throws Exception;

    Merchant findByMerchantId(String merchantId);

}
