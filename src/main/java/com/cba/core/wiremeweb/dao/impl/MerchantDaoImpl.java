package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.MerchantDao;
import com.cba.core.wiremeweb.dto.MerchantRequestDto;
import com.cba.core.wiremeweb.dto.MerchantResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.MerchantMapper;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.Merchant;
import com.cba.core.wiremeweb.model.MerchantCustomer;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.repository.MerchantRepository;
import com.cba.core.wiremeweb.repository.specification.MerchantSpecification;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class MerchantDaoImpl implements MerchantDao<MerchantResponseDto, MerchantRequestDto> {

    private final MerchantRepository repository;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final ObjectMapper objectMapper;
    private final UserBeanUtil userBeanUtil;

    @Value("${application.resource.merchants}")
    private String resource;

    @Override
    @Cacheable("merchants")
    public Page<MerchantResponseDto> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Merchant> entitiesPage = repository.findAll(pageable);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Merchants found");
        }
        return entitiesPage.map(MerchantMapper::toDto);
    }

    @Override
    @Cacheable("merchants")
    public List<MerchantResponseDto> findAll() throws Exception {
        List<Merchant> entityList = repository.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Merchants found");
        }
        return entityList
                .stream()
                .map(MerchantMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MerchantResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<Merchant> spec = MerchantSpecification.
                nameLikeAndStatusLike(searchParamList.get(0).get("merchantName"),
                        searchParamList.get(0).get("status"));

        Page<Merchant> entitiesPage = repository.findAll(spec, pageable);

        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Merchants found");
        }
        return entitiesPage.map(MerchantMapper::toDto);
    }

    @Override
    public MerchantResponseDto findById(int id) throws Exception {
        Merchant entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Merchant not found"));
        return MerchantMapper.toDto(entity);
    }

    @Override
    @CacheEvict(value = "merchants", allEntries = true)
    public MerchantResponseDto deleteById(int id) throws Exception {
        try {
            Merchant entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Merchant not found"));
            MerchantResponseDto responseDto = MerchantMapper.toDto(entity);

            repository.deleteById(id);
            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                    id, objectMapper.writeValueAsString(responseDto), null,
                    userBeanUtil.getRemoteAdr()));

            return responseDto;

        } catch (Exception rr) {
            throw rr;
        }
    }

    @Override
    @CacheEvict(value = "merchants", allEntries = true)
    public void deleteByIdList(List<Integer> idList) throws Exception {
        try {
            idList.stream()
                    .map((id) -> repository.findById(id).orElseThrow(() -> new NotFoundException("Merchant not found")))
                    .collect(Collectors.toList());

            repository.deleteAllByIdInBatch(idList);

            idList.stream()
                    .forEach(item -> {
                        ObjectNode objectNode = objectMapper.createObjectNode();
                        objectNode.put("id", item);
                        try {
                            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                                    item, objectMapper.writeValueAsString(objectNode), null,
                                    userBeanUtil.getRemoteAdr()));
                        } catch (Exception e) {
                            throw new RuntimeException("Exception occurred for Auditing: ");// only unchecked exception can be passed
                        }
                    });
        } catch (Exception ee) {
            ee.printStackTrace();
            throw ee;
        }
    }

    @Override
    @CacheEvict(value = "merchants", allEntries = true)
    public MerchantResponseDto updateById(int id, MerchantRequestDto requestDto) throws Exception {

        Merchant toBeUpdated = repository.findById(id).orElseThrow(() -> new NotFoundException("Merchant not found"));

        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        if (!toBeUpdated.getMerchantId().equals(requestDto.getMerchantId())) {
            updateRequired = true;
            oldDataMap.put("merchantId", toBeUpdated.getMerchantId());
            newDataMap.put("merchantId", requestDto.getMerchantId());

            toBeUpdated.setMerchantId(requestDto.getMerchantId());
        }
        if (toBeUpdated.getMerchantCustomer().getId() != requestDto.getPartnerId()) {
            updateRequired = true;
            oldDataMap.put("partnerId", toBeUpdated.getMerchantCustomer().getId());
            newDataMap.put("partnerId", requestDto.getPartnerId());

            toBeUpdated.setMerchantCustomer(new MerchantCustomer(requestDto.getPartnerId()));
        }
        if (!toBeUpdated.getName().equals(requestDto.getName())) {
            updateRequired = true;
            oldDataMap.put("name", toBeUpdated.getName());
            newDataMap.put("name", requestDto.getName());

            toBeUpdated.setName(requestDto.getName());
        }
        if (!toBeUpdated.getEmail().equals(requestDto.getEmail())) {
            updateRequired = true;
            oldDataMap.put("email", toBeUpdated.getEmail());
            newDataMap.put("email", requestDto.getEmail());

            toBeUpdated.setEmail(requestDto.getEmail());
        }
        if (!toBeUpdated.getDistrict().equals(requestDto.getDistrict())) {
            updateRequired = true;
            oldDataMap.put("district", toBeUpdated.getDistrict());
            newDataMap.put("district", requestDto.getDistrict());

            toBeUpdated.setDistrict(requestDto.getDistrict());
        }
        if (!toBeUpdated.getProvince().equals(requestDto.getProvince())) {
            updateRequired = true;
            oldDataMap.put("province", toBeUpdated.getProvince());
            newDataMap.put("province", requestDto.getProvince());

            toBeUpdated.setProvince(requestDto.getProvince());
        }
        if (!toBeUpdated.getStatus().getStatusCode().equals(requestDto.getStatus())) {
            updateRequired = true;
            oldDataMap.put("status", toBeUpdated.getStatus().getStatusCode());
            newDataMap.put("status", requestDto.getStatus());

            toBeUpdated.setStatus(new Status(requestDto.getStatus()));
        }
        if (updateRequired) {

            repository.saveAndFlush(toBeUpdated);
            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                    id, objectMapper.writeValueAsString(oldDataMap), objectMapper.writeValueAsString(newDataMap),
                    userBeanUtil.getRemoteAdr()));

            return MerchantMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    @CacheEvict(value = "merchants", allEntries = true)
    public MerchantResponseDto create(MerchantRequestDto requestDto) throws Exception {

        Merchant toInsert = MerchantMapper.toModel(requestDto);

        Merchant savedEntity = repository.save(toInsert);
        MerchantResponseDto responseDto = MerchantMapper.toDto(savedEntity);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    @CacheEvict(value = "merchants", allEntries = true)
    public List<MerchantResponseDto> createBulk(List<MerchantRequestDto> requestDtoList) throws Exception {

        List<Merchant> entityList = requestDtoList
                .stream()
                .map(MerchantMapper::toModel)
                .collect(Collectors.toList());

        return repository.saveAll(entityList)
                .stream()
                .map(item -> {
                    MerchantResponseDto responseDto = MerchantMapper.toDto(item);
                    try {
                        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                                item.getId(), null, objectMapper.writeValueAsString(responseDto),
                                userBeanUtil.getRemoteAdr()));
                    } catch (Exception e) {
                        throw new RuntimeException("Exception occurred in Auditing: ");// only unchecked exception can be passed
                    }
                    return responseDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<MerchantResponseDto> findMerchantsByPartner(int id, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Merchant> entitiesPage = repository.findAllByMerchantCustomer_Id(id, pageable);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Merchants found");
        }
        return entitiesPage.map(MerchantMapper::toDto);
    }
}
