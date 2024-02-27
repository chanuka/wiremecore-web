package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.model.MerchantCustomer;
import com.cba.core.wiremeweb.repository.MerchantCustomerRepository;
import com.cba.core.wiremeweb.repository.specification.MerchantCustomerSpecification;
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
public class MerchantCustomerDaoImpl implements GenericDao<MerchantCustomer> {

    private final MerchantCustomerRepository repository;

    @Override
//    @Cacheable("partners")
    public Page<MerchantCustomer> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAll(pageable);
    }

    @Override
//    @Cacheable("partners")
    public List<MerchantCustomer> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Page<MerchantCustomer> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<MerchantCustomer> spec = MerchantCustomerSpecification.
                nameLikeAndStatusLike(searchParamList.get("merchantCustomerName"),
                        searchParamList.get("status"), searchParamList.get("address"));
        return repository.findAll(spec, pageable);
    }

    @Override
    public List<MerchantCustomer> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter) throws Exception {
        Specification<MerchantCustomer> spec = MerchantCustomerSpecification.allLike(searchParameter.get("keyWord"));
        return repository.findAll(spec);
    }

    @Override
    public MerchantCustomer findById(int id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Merchant Customer not found"));
    }

    @Override
//    @CacheEvict(value = "partners", allEntries = true)
    public void deleteById(int id) throws Exception {
        repository.deleteById(id);
    }

    @Override
//    @CacheEvict(value = "partners", allEntries = true)
    public void deleteByIdList(List<Integer> idList) throws Exception {
        repository.deleteAllByIdInBatch(idList);
    }

    @Override
//    @CacheEvict(value = "partners", allEntries = true)
    public MerchantCustomer updateById(int id, MerchantCustomer requestDto) throws Exception {
        return repository.saveAndFlush(requestDto);
    }

    @Override
//    @CacheEvict(value = "partners", allEntries = true)
    public MerchantCustomer create(MerchantCustomer requestDto) throws Exception {
        return repository.save(requestDto);
    }

    @Override
//    @CacheEvict(value = "partners", allEntries = true)
    public List<MerchantCustomer> createBulk(List<MerchantCustomer> entityList) throws Exception {
        return repository.saveAll(entityList);
    }

}
