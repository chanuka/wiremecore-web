package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
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
import com.cba.core.wiremeweb.util.UserOperationEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
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
public class MerchantCustomerDaoImpl implements GenericDao<MerchantCustomerResponseDto, MerchantCustomerRequestDto> {

    private final MerchantCustomerRepository repository;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final HttpServletRequest request;
    @Value("${application.resource.partners}")
    private String resource;

    @Override
    @Cacheable("partners")
    public Page<MerchantCustomerResponseDto> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<MerchantCustomer> entitiesPage = repository.findAll(pageable);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Merchant Customers found");
        }
        return entitiesPage.map(MerchantCustomerMapper::toDto);
    }

    @Override
    @Cacheable("partners")
    public List<MerchantCustomerResponseDto> findAll() throws Exception {
        List<MerchantCustomer> entityList = repository.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Merchant Customers found");
        }
        return entityList
                .stream()
                .map(MerchantCustomerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MerchantCustomerResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<MerchantCustomer> spec = MerchantCustomerSpecification.
                terminalIdLikeAndMerchantIdLike(searchParamList.get(0).get("merchantCustomerName"),
                        searchParamList.get(0).get("status"));

        Page<MerchantCustomer> entitiesPage = repository.findAll(spec, pageable);

        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Merchant Customers found");
        }
        return entitiesPage.map(MerchantCustomerMapper::toDto);
    }

    @Override
    public MerchantCustomerResponseDto findById(int id) throws Exception {
        MerchantCustomer entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Merchant Customer not found"));
        return MerchantCustomerMapper.toDto(entity);
    }

    @Override
    @CacheEvict(value = "partners", allEntries = true)
    public MerchantCustomerResponseDto deleteById(int id) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MerchantCustomer entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Merchant Customer not found"));
            MerchantCustomerResponseDto merchantCustomerResponseDto = MerchantCustomerMapper.toDto(entity);

            repository.deleteById(id);
            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                    id, objectMapper.writeValueAsString(merchantCustomerResponseDto), null,
                    request.getRemoteAddr()));

            return merchantCustomerResponseDto;

        } catch (Exception rr) {
            throw rr;
        }
    }

    @Override
    @CacheEvict(value = "partners", allEntries = true)
    public void deleteByIdList(List<Integer> idList) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String remoteAdr = request.getRemoteAddr();

            idList.stream()
                    .map((id) -> repository.findById(id).orElseThrow(() -> new NotFoundException("Merchant Customer not found")))
                    .collect(Collectors.toList());

            repository.deleteAllByIdInBatch(idList);

            idList.stream()
                    .forEach(item -> {
                        ObjectNode objectNode = objectMapper.createObjectNode();
                        objectNode.put("id", item);
                        try {
                            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                                    item, objectMapper.writeValueAsString(objectNode), null,
                                    remoteAdr));
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
    @CacheEvict(value = "partners", allEntries = true)
    public MerchantCustomerResponseDto updateById(int id, MerchantCustomerRequestDto requestDto) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String remoteAdr = request.getRemoteAddr();
        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        MerchantCustomer toBeUpdated = repository.findById(id).orElseThrow(() -> new NotFoundException("Merchant Customer not found"));

        if (!toBeUpdated.getName().equals(requestDto.getName())) {
            updateRequired = true;
            oldDataMap.put("name", toBeUpdated.getName());
            newDataMap.put("name", requestDto.getName());

            toBeUpdated.setName(requestDto.getName());
        }
        if (!toBeUpdated.getAddress().equals(requestDto.getAddress())) {
            updateRequired = true;
            oldDataMap.put("address", toBeUpdated.getAddress());
            newDataMap.put("address", requestDto.getAddress());

            toBeUpdated.setAddress(requestDto.getAddress());
        }
        if (!toBeUpdated.getContactNo().equals(requestDto.getContactNo())) {
            updateRequired = true;
            oldDataMap.put("contactNo", toBeUpdated.getContactNo());
            newDataMap.put("contactNo", requestDto.getContactNo());

            toBeUpdated.setContactNo(requestDto.getContactNo());
        }
        if (!toBeUpdated.getEmail().equals(requestDto.getEmail())) {
            updateRequired = true;
            oldDataMap.put("email", toBeUpdated.getEmail());
            newDataMap.put("email", requestDto.getEmail());

            toBeUpdated.setEmail(requestDto.getEmail());
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
                    remoteAdr));

            return MerchantCustomerMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    @CacheEvict(value = "partners", allEntries = true)
    public MerchantCustomerResponseDto create(MerchantCustomerRequestDto requestDto) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String remoteAdr = request.getRemoteAddr();

        MerchantCustomer toInsert = MerchantCustomerMapper.toModel(requestDto);

        MerchantCustomer savedEntity = repository.save(toInsert);
        MerchantCustomerResponseDto responseDto = MerchantCustomerMapper.toDto(savedEntity);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                remoteAdr));

        return responseDto;
    }

    @Override
    @CacheEvict(value = "partners", allEntries = true)
    public List<MerchantCustomerResponseDto> createBulk(List<MerchantCustomerRequestDto> requestDtoList) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String remoteAdr = request.getRemoteAddr();

        List<MerchantCustomer> entityList = requestDtoList
                .stream()
                .map(MerchantCustomerMapper::toModel)
                .collect(Collectors.toList());

        return repository.saveAll(entityList)
                .stream()
                .map(item -> {
                    MerchantCustomerResponseDto responseDto = MerchantCustomerMapper.toDto(item);
                    try {
                        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                                item.getId(), null, objectMapper.writeValueAsString(responseDto),
                                remoteAdr));
                    } catch (Exception e) {
                        throw new RuntimeException("Exception occurred in Auditing: ");// only unchecked exception can be passed
                    }
                    return responseDto;
                })
                .collect(Collectors.toList());
    }
}
