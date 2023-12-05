package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.TerminalDao;
import com.cba.core.wiremeweb.dto.TerminalRequestDto;
import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.TerminalMapper;
import com.cba.core.wiremeweb.model.*;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.repository.MerchantRepository;
import com.cba.core.wiremeweb.repository.TerminalRepository;
import com.cba.core.wiremeweb.repository.specification.TerminalSpecification;
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
public class TerminalDaoImpl implements TerminalDao<TerminalResponseDto, TerminalRequestDto> {

    private final TerminalRepository repository;
    private final MerchantRepository merchantRepository;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final ObjectMapper objectMapper;
    private final UserBeanUtil userBeanUtil;

    @Value("${application.resource.terminals}")
    private String resource;

    @Override
    @Cacheable("terminals")
    public Page<TerminalResponseDto> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Terminal> entitiesPage = repository.findAll(pageable);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Terminals found");
        }
        return entitiesPage.map(TerminalMapper::toDto);
    }

    @Override
    @Cacheable("terminals")
    public List<TerminalResponseDto> findAll() throws Exception {
        List<Terminal> entityList = repository.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Terminals found");
        }
        return entityList
                .stream()
                .map(TerminalMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TerminalResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {

        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<Terminal> spec = TerminalSpecification.
                terminalIdLikeAndMerchantIdLike(searchParamList.get(0).get("terminalId"),
                        searchParamList.get(0).get("merchantId"));

        Page<Terminal> entitiesPage = repository.findAll(spec, pageable);

        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Terminals found");
        }
        return entitiesPage.map(TerminalMapper::toDto);
    }

    @Override
    public TerminalResponseDto findById(int id) throws Exception {
        Terminal entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Terminal not found"));
        return TerminalMapper.toDto(entity);
    }

    @Override
    @CacheEvict(value = "terminals", allEntries = true)
    public TerminalResponseDto deleteById(int id) throws Exception {
        try {
            Terminal entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Terminal not found"));
            TerminalResponseDto responseDto = TerminalMapper.toDto(entity);

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
    @CacheEvict(value = "terminals", allEntries = true)
    public void deleteByIdList(List<Integer> idList) throws Exception {
        try {

            idList.stream()
                    .map((id) -> repository.findById(id).orElseThrow(() -> new NotFoundException("Terminal not found")))
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
    @CacheEvict(value = "terminals", allEntries = true)
    public TerminalResponseDto updateById(int id, TerminalRequestDto requestDto) throws Exception {

        Terminal toBeUpdated = repository.findById(id).orElseThrow(() -> new NotFoundException("Terminal not found"));

        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        if (!toBeUpdated.getTerminalId().equals(requestDto.getTerminalId())) {
            updateRequired = true;
            oldDataMap.put("terminalId", toBeUpdated.getTerminalId());
            newDataMap.put("terminalId", requestDto.getTerminalId());

            toBeUpdated.setTerminalId(requestDto.getTerminalId());
        }
        if (!toBeUpdated.getMerchant().getMerchantId().equals(requestDto.getMerchantId())) {
            updateRequired = true;
            oldDataMap.put("merchantId", toBeUpdated.getMerchant().getMerchantId());
            newDataMap.put("merchantId", requestDto.getMerchantId());
            Merchant merchant = merchantRepository.findByMerchantId(requestDto.getMerchantId())
                    .orElseThrow(() -> new NotFoundException("Merchant Not Found"));
            toBeUpdated.setMerchant(merchant);
        }
        if (toBeUpdated.getDevice().getId() != requestDto.getDeviceId()) {
            updateRequired = true;
            oldDataMap.put("deviceId", toBeUpdated.getDevice().getId());
            newDataMap.put("deviceId", requestDto.getDeviceId());

            toBeUpdated.setDevice(new Device(requestDto.getDeviceId()));
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

            return TerminalMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    @CacheEvict(value = "terminals", allEntries = true)
    public TerminalResponseDto create(TerminalRequestDto requestDto) throws Exception {


        Merchant merchant = merchantRepository.findByMerchantId(requestDto.getMerchantId())
                .orElseThrow(() -> new NotFoundException("Merchant Not Found"));

        Terminal toInsert = TerminalMapper.toModel(requestDto, merchant);

        Terminal savedEntity = repository.save(toInsert);
        TerminalResponseDto responseDto = TerminalMapper.toDto(savedEntity);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    @CacheEvict(value = "terminals", allEntries = true)
    public List<TerminalResponseDto> createBulk(List<TerminalRequestDto> requestDtoList) throws Exception {

        List<Terminal> entityList = requestDtoList
                .stream()
                .map((terminalDto) -> {
                    Merchant merchant = merchantRepository.findByMerchantId(terminalDto.getMerchantId())
                            .orElseThrow(() -> new NotFoundException("Merchant Not Found"));
                    return TerminalMapper.toModel(terminalDto, merchant);
                })
                .collect(Collectors.toList());

        return repository.saveAll(entityList)
                .stream()
                .map(item -> {
                    TerminalResponseDto responseDto = TerminalMapper.toDto(item);
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
    public Page<TerminalResponseDto> findTerminalsByMerchant(int id, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Terminal> entitiesPage = repository.findAllByMerchant_Id(id, pageable);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Terminals found");
        }
        return entitiesPage.map(TerminalMapper::toDto);
    }
}
