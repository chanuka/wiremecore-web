package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant, Integer>, JpaSpecificationExecutor<Merchant> {

    Page<Merchant> findAllByMerchantCustomer_Id(int id, Pageable pageable);

    Optional<Merchant> findByMerchantId(String merchantId);

}
