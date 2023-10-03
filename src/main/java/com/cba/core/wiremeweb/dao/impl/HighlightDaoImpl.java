package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.HighlightDao;
import com.cba.core.wiremeweb.dto.HighlightRequestDto;
import com.cba.core.wiremeweb.dto.HighlightResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.HighlightMapper;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.model.UserConfig;
import com.cba.core.wiremeweb.repository.DashBoardRepository;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.repository.TransactionRepository;
import com.cba.core.wiremeweb.repository.UserRepository;
import com.cba.core.wiremeweb.util.UserBean;
import com.cba.core.wiremeweb.util.UserOperationEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class HighlightDaoImpl implements HighlightDao {

    private final DashBoardRepository repository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final UserBean userBean;
    private final ObjectMapper objectMapper;

    @Value("${application.resource.users}")
    private String resource;

    @Override
    public List<HighlightResponseDto> findAll(String configType) throws Exception {

        List<UserConfig> entityList = repository.findByUser_NameAndConfigType(userBean.getUsername(), configType);
        if (entityList.isEmpty()) {
            throw new NotFoundException("No User Config found");
        }

        return entityList
                .stream()
                .map((userConfig -> {
                    try {
                        HighlightResponseDto responseDto = objectMapper.readValue(userConfig.getConfig(), HighlightResponseDto.class);
                        return HighlightMapper.toDto(responseDto, userConfig);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }))
                .collect(Collectors.toList());
    }

    @Override
    public HighlightResponseDto deleteByUser_NameAndConfigType(String configName) throws Exception {
        try {

            UserConfig entity = repository.findByUser_NameAndConfigName(userBean.getUsername(), configName)
                    .orElseThrow(() -> new NotFoundException("User Config not found"));

            HighlightResponseDto responseDto = objectMapper.readValue(entity.getConfig(), HighlightResponseDto.class);

            repository.deleteByUser_NameAndConfigName(userBean.getUsername(), configName);

            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                    entity.getUser().getId(), entity.getConfig(), null,
                    userBean.getRemoteAdr()));

            return HighlightMapper.toDto(responseDto, entity);

        } catch (Exception rr) {
            rr.printStackTrace();
            throw rr;
        }
    }

    @Override
    public HighlightResponseDto create(HighlightRequestDto requestDto) throws Exception {

        String config = objectMapper.writeValueAsString(requestDto);
        UserConfig toInsert = HighlightMapper.toModel(requestDto, config, userRepository.findByUserName(userBean.getUsername()));

        UserConfig savedEntity = repository.save(toInsert);

        HighlightResponseDto responseDto = objectMapper.readValue(savedEntity.getConfig(), HighlightResponseDto.class);

        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBean.getRemoteAdr()));

        return responseDto;
    }

    @Override
    public HighlightResponseDto update(String configName, HighlightRequestDto requestDto) throws Exception {

        UserConfig toBeUpdatedEntity = repository.findByUser_NameAndConfigName(userBean.getUsername(), configName).orElseThrow(() -> new NotFoundException("User Config not found"));
        HighlightResponseDto toBeUpdatedDto = objectMapper.readValue(toBeUpdatedEntity.getConfig(), HighlightResponseDto.class);

        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        if (!toBeUpdatedEntity.getConfig().equals(objectMapper.writeValueAsString(requestDto))) {

            if (!toBeUpdatedEntity.getStatus().getStatusCode().equals(requestDto.getStatus())) {
                updateRequired = true;
                oldDataMap.put("status", toBeUpdatedEntity.getStatus().getStatusCode());
                newDataMap.put("status", requestDto.getStatus());

                toBeUpdatedEntity.setStatus(new Status(requestDto.getStatus()));
                toBeUpdatedDto.setStatus(requestDto.getStatus());
            }
            if (!toBeUpdatedEntity.getConfigType().equals(requestDto.getConfigType())) {
                updateRequired = true;
                oldDataMap.put("configType", toBeUpdatedEntity.getConfigType());
                newDataMap.put("configType", requestDto.getConfigType());

                toBeUpdatedEntity.setConfigType(requestDto.getConfigType());
                toBeUpdatedDto.setConfigType(requestDto.getConfigType());
            }
            if (toBeUpdatedEntity.getPriorityOrder() != requestDto.getPriorityOrder()) {
                updateRequired = true;
                oldDataMap.put("priorityOrder", toBeUpdatedEntity.getPriorityOrder());
                newDataMap.put("priorityOrder", requestDto.getPriorityOrder());

                toBeUpdatedEntity.setPriorityOrder(requestDto.getPriorityOrder());
                toBeUpdatedDto.setPriorityOrder(requestDto.getPriorityOrder());
            }
            if (!toBeUpdatedDto.getAggregator().equals(requestDto.getAggregator())) {
                updateRequired = true;
                oldDataMap.put("aggregator", toBeUpdatedDto.getAggregator());
                newDataMap.put("aggregator", requestDto.getAggregator());

                toBeUpdatedDto.setAggregator(requestDto.getAggregator());
            }
            if (!toBeUpdatedDto.getConfigTitle().equals(requestDto.getConfigTitle())) {
                updateRequired = true;
                oldDataMap.put("configTitle", toBeUpdatedDto.getConfigTitle());
                newDataMap.put("configTitle", requestDto.getConfigTitle());

                toBeUpdatedDto.setConfigTitle(requestDto.getConfigTitle());
            }
            if (!toBeUpdatedDto.getGrouping().equals(requestDto.getGrouping())) {
                updateRequired = true;
                oldDataMap.put("configTitle", toBeUpdatedDto.getGrouping());
                newDataMap.put("configTitle", requestDto.getGrouping());

                toBeUpdatedDto.setGrouping(requestDto.getGrouping());
            }
            if (!toBeUpdatedDto.getDateClustering().equals(requestDto.getDateClustering())) {
                updateRequired = true;
                oldDataMap.put("dateClustering", toBeUpdatedDto.getDateClustering());
                newDataMap.put("dateClustering", requestDto.getDateClustering());

                toBeUpdatedDto.setDateClustering(requestDto.getDateClustering());
            }
            if (!toBeUpdatedDto.getTag().equals(requestDto.getTag())) {
                updateRequired = true;
                oldDataMap.put("tag", toBeUpdatedDto.getTag());
                newDataMap.put("tag", requestDto.getTag());

                toBeUpdatedDto.setTag(requestDto.getTag());
            }
            if (!toBeUpdatedDto.getSelectionScope().getDistrict().equals(requestDto.getSelectionScope().getDistrict())) {
                updateRequired = true;
                oldDataMap.put("district", toBeUpdatedDto.getSelectionScope().getDistrict());
                newDataMap.put("district", requestDto.getSelectionScope().getDistrict());

                toBeUpdatedDto.getSelectionScope().setDistrict(requestDto.getSelectionScope().getDistrict());
            }
            if (!toBeUpdatedDto.getSelectionScope().getMerchant().equals(requestDto.getSelectionScope().getMerchant())) {
                updateRequired = true;
                oldDataMap.put("merchant", toBeUpdatedDto.getSelectionScope().getMerchant());
                newDataMap.put("merchant", requestDto.getSelectionScope().getMerchant());

                toBeUpdatedDto.getSelectionScope().setMerchant(requestDto.getSelectionScope().getMerchant());
            }
            if (!toBeUpdatedDto.getSelectionScope().getPartner().equals(requestDto.getSelectionScope().getPartner())) {
                updateRequired = true;
                oldDataMap.put("partner", toBeUpdatedDto.getSelectionScope().getPartner());
                newDataMap.put("partner", requestDto.getSelectionScope().getPartner());

                toBeUpdatedDto.getSelectionScope().setPartner(requestDto.getSelectionScope().getPartner());
            }
            if (!toBeUpdatedDto.getSelectionScope().getProvince().equals(requestDto.getSelectionScope().getProvince())) {
                updateRequired = true;
                oldDataMap.put("province", toBeUpdatedDto.getSelectionScope().getProvince());
                newDataMap.put("province", requestDto.getSelectionScope().getProvince());

                toBeUpdatedDto.getSelectionScope().setProvince(requestDto.getSelectionScope().getProvince());
            }
        }
        if (updateRequired) {

            toBeUpdatedEntity.setConfig(objectMapper.writeValueAsString(toBeUpdatedDto));
            repository.saveAndFlush(toBeUpdatedEntity);
            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                    toBeUpdatedEntity.getId(), objectMapper.writeValueAsString(oldDataMap), objectMapper.writeValueAsString(newDataMap),
                    userBean.getRemoteAdr()));

            return HighlightMapper.toDto(toBeUpdatedDto, toBeUpdatedEntity);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    public Map<Integer, Map<String, Object>> findHighLights(HighlightRequestDto requestDto) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<Integer, Map<String, Object>> responseData = new HashMap<>();

        try {
            Date fromDate = dateFormat.parse(requestDto.getFromDate());
            Date toDate = dateFormat.parse(requestDto.getToDate());

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("metaData", requestDto);
            responseData.put(0, metadata);

//            Specification<TransactionCore> spec = TransactionSpecification.fromDateAndToDate(
//                    fromDate,
//                    toDate);

//            List<TransactionCore> transactions = transactionRepository.findAll(spec);
//            System.out.println("Transaction Count : " + transactions.size());


            if ("Revenue".equals(requestDto.getAggregator())) {
                if ("CardLabel".equals(requestDto.getGrouping())) {
                    List<Object[]> revenues = transactionRepository.revenueTransactionCoreGroupByCardLabel(fromDate, toDate);
                    for (int i = 0; i < revenues.size(); i++) {
                        responseData.put(i + 1, createDataEntry(requestDto,(Long) revenues.get(i)[1], (String) revenues.get(i)[0]));
                    }
                } else if ("PaymentMode".equals(requestDto.getGrouping())) {
                    List<Object[]> revenues = transactionRepository.revenueTransactionCoreGroupByPaymentMode(fromDate, toDate);
                    for (int i = 0; i < revenues.size(); i++) {
                        responseData.put(i + 1, createDataEntry(requestDto,(Long) revenues.get(i)[1], (String) revenues.get(i)[0]));
                    }
                } else if ("TranType".equals(requestDto.getGrouping())) {
                    List<Object[]> revenues = transactionRepository.revenueTransactionCoreGroupByTranType(fromDate, toDate);
                    for (int i = 0; i < revenues.size(); i++) {
                        responseData.put(i + 1, createDataEntry(requestDto,(Long) revenues.get(i)[1], (String) revenues.get(i)[0]));
                    }
                } else {
                    return null;
                }
            } else if ("Count".equals(requestDto.getAggregator())) {
                if ("CardLabel".equals(requestDto.getGrouping())) {
                    List<Object[]> counts = transactionRepository.countTransactionCoreGroupByCardLabel(fromDate, toDate);
                    for (int i = 0; i < counts.size(); i++) {
                        responseData.put(i + 1, createDataEntry(requestDto,(Long) counts.get(i)[1], (String) counts.get(i)[0]));
                    }
                } else if ("PaymentMode".equals(requestDto.getGrouping())) {
                    List<Object[]> counts = transactionRepository.countTransactionCoreGroupByPaymentMode(fromDate, toDate);
                    for (int i = 0; i < counts.size(); i++) {
                        responseData.put(i + 1, createDataEntry(requestDto,(Long) counts.get(i)[1], (String) counts.get(i)[0]));
                    }
                } else if ("TranType".equals(requestDto.getGrouping())) {
                    List<Object[]> counts = transactionRepository.countTransactionCoreGroupByTranType(fromDate, toDate);
                    for (int i = 0; i < counts.size(); i++) {
                        responseData.put(i + 1, createDataEntry(requestDto,(Long) counts.get(i)[1], (String) counts.get(i)[0]));
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return responseData;
    }

    private Map<String, Object> createDataEntry(HighlightRequestDto requestDto, long count, String cardLabel) {
        Map<String, Object> dataEntry = new HashMap<>();
        dataEntry.put(requestDto.getAggregator(), count);
        dataEntry.put(requestDto.getGrouping(), cardLabel);
        return dataEntry;
    }
}
