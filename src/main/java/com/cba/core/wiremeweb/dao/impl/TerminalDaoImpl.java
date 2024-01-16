package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.TerminalDao;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.model.Terminal;
import com.cba.core.wiremeweb.repository.TerminalRepository;
import com.cba.core.wiremeweb.repository.specification.TerminalSpecification;
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
public class TerminalDaoImpl implements TerminalDao<Terminal, Terminal> {

    private final TerminalRepository repository;


    @Override
    @Cacheable("terminals")
    public Page<Terminal> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAll(pageable);
    }

    @Override
    @Cacheable("terminals")
    public List<Terminal> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Page<Terminal> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {

        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<Terminal> spec = TerminalSpecification.
                terminalIdLikeAndMerchantIdLike(
                        searchParamList.get("terminalId"),
                        searchParamList.get("merchantId"),
                        searchParamList.get("merchantName"),
                        searchParamList.get("serialNo"),
                        searchParamList.get("status")
                );

        return repository.findAll(spec, pageable);
    }

    @Override
    public Page<Terminal> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public Terminal findById(int id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Terminal not found"));
    }

    @Override
    @CacheEvict(value = "terminals", allEntries = true)
    public void deleteById(int id) throws Exception {
        repository.deleteById(id);
    }

    @Override
    @CacheEvict(value = "terminals", allEntries = true)
    public void deleteByIdList(List<Integer> idList) throws Exception {
        repository.deleteAllByIdInBatch(idList);
    }

    @Override
    @CacheEvict(value = "terminals", allEntries = true)
    public Terminal updateById(int id, Terminal toBeUpdated) throws Exception {
        return repository.saveAndFlush(toBeUpdated);
    }

    @Override
    @CacheEvict(value = "terminals", allEntries = true)
    public Terminal create(Terminal toInsert) throws Exception {
        return repository.save(toInsert);
    }

    @Override
    @CacheEvict(value = "terminals", allEntries = true)
    public List<Terminal> createBulk(List<Terminal> entityList) throws Exception {
        return repository.saveAll(entityList);
    }

    @Override
    public Page<Terminal> findTerminalsByMerchant(int id, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAllByMerchant_Id(id, pageable);
    }
}
