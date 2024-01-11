package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.DeviceModel;
import com.cba.core.wiremeweb.model.MerchantCustomer;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface MerchantCustomerDao<T, K> extends GenericDao<T, K> {
    Page<MerchantCustomer> findAll(Map<String, String> searchParamList, int page, int pageSize) throws Exception;
}
