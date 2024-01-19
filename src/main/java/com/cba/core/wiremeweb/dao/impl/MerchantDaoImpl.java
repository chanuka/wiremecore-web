package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.MerchantDao;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.model.Merchant;
import com.cba.core.wiremeweb.repository.MerchantRepository;
import com.cba.core.wiremeweb.repository.specification.MerchantSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MerchantDaoImpl implements MerchantDao<Merchant> {

    private final MerchantRepository repository;

    @Override
    @Cacheable("merchants")
    public Page<Merchant> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAll(pageable);
    }

    @Override
    @Cacheable("merchants")
    public List<Merchant> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Page<Merchant> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<Merchant> spec = MerchantSpecification.
                nameLikeAndStatusLike(searchParamList.get("merchantName"),
                        searchParamList.get("status"),
                        searchParamList.get("merchantId"),
                        searchParamList.get("partnerName"));
        return repository.findAll(spec, pageable);

    }

    @Override
    public List<Merchant> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter) throws Exception {
        Specification<Merchant> spec = MerchantSpecification.allLike(searchParameter.get("keyWord"));
        return repository.findAll(spec);
    }

    @Override
    public Merchant findById(int id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Merchant not found"));
    }

    @Override
    @CacheEvict(value = "merchants", allEntries = true)
    public void deleteById(int id) throws Exception {
        repository.deleteById(id);
    }

    @Override
    @CacheEvict(value = "merchants", allEntries = true)
    public void deleteByIdList(List<Integer> idList) throws Exception {
        repository.deleteAllByIdInBatch(idList);
    }

    @Override
    @CacheEvict(value = "merchants", allEntries = true)
    public Merchant updateById(int id, Merchant toBeUpdated) throws Exception {
        return repository.saveAndFlush(toBeUpdated);
    }

    @Override
    @CacheEvict(value = "merchants", allEntries = true)
    public Merchant create(Merchant toInsert) throws Exception {
        return repository.save(toInsert);
    }

    @Override
    @CacheEvict(value = "merchants", allEntries = true)
    public List<Merchant> createBulk(List<Merchant> entityList) throws Exception {
        return repository.saveAll(entityList);
    }

    @Override
    public Page<Merchant> findMerchantsByPartner(int id, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAllByMerchantCustomer_Id(id, pageable);
    }

    @Override
    public Merchant findByMerchantId(String merchantId) {
        return repository.findByMerchantId(merchantId).orElseThrow(() -> new NotFoundException("Merchant Not Found"));
    }
}
