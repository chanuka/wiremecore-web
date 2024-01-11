package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dao.MerchantCustomerDao;
import com.cba.core.wiremeweb.dto.MerchantCustomerRequestDto;
import com.cba.core.wiremeweb.dto.MerchantCustomerResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.MerchantCustomerMapper;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.MerchantCustomer;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.repository.MerchantCustomerRepository;
import com.cba.core.wiremeweb.repository.specification.MerchantCustomerSpecification;
import com.cba.core.wiremeweb.util.UserBeanUtil;
import com.cba.core.wiremeweb.util.UserOperationEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MerchantCustomerDaoImpl implements MerchantCustomerDao<MerchantCustomer, MerchantCustomer> {

    private final MerchantCustomerRepository repository;

    @Override
    @Cacheable("partners")
    public Page<MerchantCustomer> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAll(pageable);
    }

    @Override
    @Cacheable("partners")
    public List<MerchantCustomer> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Page<MerchantCustomer> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public Page<MerchantCustomer> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public MerchantCustomer findById(int id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Merchant Customer not found"));
    }

    @Override
    @CacheEvict(value = "partners", allEntries = true)
    public MerchantCustomer deleteById(int id) throws Exception {
        repository.deleteById(id);

        return new MerchantCustomer();
    }

    @Override
    @CacheEvict(value = "partners", allEntries = true)
    public void deleteByIdList(List<Integer> idList) throws Exception {
        repository.deleteAllByIdInBatch(idList);
    }

    @Override
    @CacheEvict(value = "partners", allEntries = true)
    public MerchantCustomer updateById(int id, MerchantCustomer requestDto) throws Exception {
        return repository.saveAndFlush(requestDto);
    }

    @Override
    @CacheEvict(value = "partners", allEntries = true)
    public MerchantCustomer create(MerchantCustomer requestDto) throws Exception {
        return repository.save(requestDto);
    }

    @Override
    @CacheEvict(value = "partners", allEntries = true)
    public List<MerchantCustomer> createBulk(List<MerchantCustomer> entityList) throws Exception {
        return repository.saveAll(entityList);
    }

    @Override
    public Page<MerchantCustomer> findAll(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<MerchantCustomer> spec = MerchantCustomerSpecification.
                nameLikeAndStatusLike(searchParamList.get("merchantCustomerName"),
                        searchParamList.get("status"), searchParamList.get("address"));
        return repository.findAll(spec, pageable);
    }
}
