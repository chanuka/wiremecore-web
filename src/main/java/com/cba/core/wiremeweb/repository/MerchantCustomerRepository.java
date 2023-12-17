package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.MerchantCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantCustomerRepository extends JpaRepository<MerchantCustomer, Integer>, JpaSpecificationExecutor<MerchantCustomer> {
}
