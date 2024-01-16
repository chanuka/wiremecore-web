package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.GlobalAuditDao;
import com.cba.core.wiremeweb.dao.HighlightDao;
import com.cba.core.wiremeweb.dao.UserDao;
import com.cba.core.wiremeweb.dto.HighlightRequestDto;
import com.cba.core.wiremeweb.dto.HighlightResponseDto;
import com.cba.core.wiremeweb.dto.TransactionCoreResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.HighlightMapper;
import com.cba.core.wiremeweb.mapper.TransactionCoreMapper;
import com.cba.core.wiremeweb.model.*;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.repository.UserRepository;
import com.cba.core.wiremeweb.service.HighlightService;
import com.cba.core.wiremeweb.util.UserBeanUtil;
import com.cba.core.wiremeweb.util.UserOperationEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
@RequiredArgsConstructor
public class HighlightServiceImpl implements HighlightService {

    private final HighlightDao dao;
    private final UserDao<User, User> userDao;
    private final GlobalAuditDao globalAuditDao;
    private final UserBeanUtil userBeanUtil;
    private final ObjectMapper objectMapper;

    @Value("${application.resource.users}")
    private String resource;

    @Override
    public List<HighlightResponseDto> findAll(String configType) throws Exception {
        List<UserConfig> entityList = dao.findAll(userBeanUtil.getUsername(), configType);
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

        UserConfig entity = dao.findByUser_NameAndConfigName(userBeanUtil.getUsername(), configName);
        HighlightResponseDto responseDto = objectMapper.readValue(entity.getConfig(), HighlightResponseDto.class);

        dao.deleteByUser_NameAndConfigName(userBeanUtil.getUsername(), configName);

        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                entity.getUser().getId(), entity.getConfig(), null,
                userBeanUtil.getRemoteAdr()));

        return HighlightMapper.toDto(responseDto, entity);
    }

    @Override
    public HighlightResponseDto create(HighlightRequestDto requestDto) throws Exception {
        String config = objectMapper.writeValueAsString(requestDto);
        User user = userDao.findByUserName(userBeanUtil.getUsername());
        UserConfig toInsert = HighlightMapper.toModel(requestDto, config, user);

        UserConfig savedEntity = dao.create(toInsert);

        HighlightResponseDto responseDto = objectMapper.readValue(savedEntity.getConfig(), HighlightResponseDto.class);

        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    public HighlightResponseDto update(String configName, HighlightRequestDto requestDto) throws Exception {
//        return dao.update(configName, requestDto);
        UserConfig toBeUpdatedEntity = dao.findByUser_NameAndConfigName(userBeanUtil.getUsername(), configName);
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
            dao.update(configName, toBeUpdatedEntity);
            globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                    toBeUpdatedEntity.getId(), objectMapper.writeValueAsString(oldDataMap), objectMapper.writeValueAsString(newDataMap),
                    userBeanUtil.getRemoteAdr()));

            return HighlightMapper.toDto(toBeUpdatedDto, toBeUpdatedEntity);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    public Map<String, Map<String, Object>> findHighLights(HighlightRequestDto requestDto) throws Exception {
        Map<String, Map<String, Object>> responseData = new HashMap<>();

        try {

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("metaData", requestDto);
            responseData.put("0", metadata);

            String whereClause = setWhereCondition(requestDto);
            String selectClause = setSelectCondition(requestDto);
            String groupByClause = setGroupByCondition(requestDto);

            List<Object[]> list = dao.findHighLights(whereClause, selectClause, groupByClause, requestDto);
            extracted(requestDto, responseData, list);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return responseData;
    }

    @Override
    public Map<String, TransactionCoreResponseDto> findHighLightsDetail(HighlightRequestDto requestDto) throws Exception {
        Map<String, TransactionCoreResponseDto> responseData = new HashMap<>();

        try {

            String whereClause = setWhereCondition(requestDto);
            List<TransactionCore> list = dao.findHighLightsDetail(whereClause, requestDto);

            IntStream.range(0, list.size())
                    .forEach(i -> {
                        String index = String.valueOf(i + 1);
                        TransactionCore value = list.get(i);
                        responseData.put(index, TransactionCoreMapper.toDto(value));
                    });

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return responseData;
    }

    private String setGroupByCondition(HighlightRequestDto requestDto) throws Exception {

        String grouping = requestDto.getGrouping();
        String groupBy = " ";
        if (grouping != null && !"".equalsIgnoreCase(grouping)) {
            if ("CardLabel".equalsIgnoreCase(grouping)) {
                groupBy += " p.cardLabel";
            }
            if ("PaymentMode".equalsIgnoreCase(grouping)) {
                groupBy += " p.paymentMode";
            }
            if ("TranType".equalsIgnoreCase(grouping)) {
                groupBy += " p.tranType";
            }
        } else {
        }

        return groupBy;
    }

    private String setSelectCondition(HighlightRequestDto requestDto) throws Exception {

        String aggregator = requestDto.getAggregator();
        String grouping = requestDto.getGrouping();

        String select = " ";

        if ((aggregator != null && !"".equals(aggregator)) && (grouping != null && !"".equals(grouping))) {
            if ("CardLabel".equalsIgnoreCase(grouping)) {
                select += " p.cardLabel,";
                if ("Revenue".equalsIgnoreCase(aggregator)) {
                    select += " sum(p.amount) ";
                }
                if ("Count".equalsIgnoreCase(aggregator)) {
                    select += " count(p) ";
                }
            }
            if ("PaymentMode".equalsIgnoreCase(grouping)) {
                select += " p.paymentMode,";
                if ("Revenue".equalsIgnoreCase(aggregator)) {
                    select += " sum(p.amount) ";
                }
                if ("Count".equalsIgnoreCase(aggregator)) {
                    select += " count(p) ";
                }
            }
            if ("TranType".equalsIgnoreCase(grouping)) {
                select += " p.tranType,";
                if ("Revenue".equalsIgnoreCase(aggregator)) {
                    select += " sum(p.amount) ";
                }
                if ("Count".equalsIgnoreCase(aggregator)) {
                    select += " count(p) ";
                }
            }

        } else {
        }

        return select;
    }

    private String setWhereCondition(HighlightRequestDto requestDto) throws Exception {

        String fromDate = requestDto.getFromDate();
        String toDate = requestDto.getToDate();
        String partner = requestDto.getSelectionScope().getPartner();
        String merchant = requestDto.getSelectionScope().getMerchant();
        String province = requestDto.getSelectionScope().getProvince();
        String district = requestDto.getSelectionScope().getDistrict();
        String filterKey = "", filterValue = "";

        if (requestDto.getFilter() != null) {
            for (Map.Entry<String, String> entry : requestDto.getFilter().entrySet()) {
                filterKey = entry.getKey();
                filterValue = entry.getValue();
            }
        }
        String where = " 1=1 ";

        if (partner != null && !"all".equalsIgnoreCase(partner)) {
            where += " AND m.merchantCustomer.name=:partner";
        }
        if (merchant != null && !"all".equalsIgnoreCase(merchant)) {
            where += " AND m.merchantId=:merchant";
        }
        if (province != null && !"all".equalsIgnoreCase(province)) {
            where += " AND m.province=:province";
        }
        if (district != null && !"all".equalsIgnoreCase(district)) {
            where += " AND m.district=:district";
        }
        if ((fromDate != null && !fromDate.isEmpty())
                && (toDate != null && !toDate.isEmpty())) {
            where += " AND p.dateTime BETWEEN :fromDate AND :toDate ";
        }
        if (filterKey != null && !"".equals(filterKey) && filterValue != null && !"".equals(filterValue)) {
            if ("CardLabel".equalsIgnoreCase(filterKey)) {
                where += " AND p.cardLabel=:filterValue";
            }
            if ("PaymentMode".equalsIgnoreCase(filterKey)) {
                where += " AND p.paymentMode=:filterValue";
            }
            if ("TranType".equalsIgnoreCase(filterKey)) {
                where += " AND p.tranType=:filterValue";
            }
        } else {
        }

        return where;
    }

    private void extracted(HighlightRequestDto requestDto, Map<String, Map<String, Object>> responseData, List<Object[]> list) {
        IntStream.range(0, list.size())
                .forEach(i -> {
                    String index = String.valueOf(i + 1);
                    Long value = (Long) list.get(i)[1];
                    String label = (String) list.get(i)[0];
                    responseData.put(index, createDataEntry(requestDto, value, label)
                    );
                });
    }

    private Map<String, Object> createDataEntry(HighlightRequestDto requestDto, long count, String cardLabel) {
        Map<String, Object> dataEntry = new HashMap<>();
        dataEntry.put(requestDto.getAggregator(), count);
        dataEntry.put(requestDto.getGrouping(), cardLabel);
        return dataEntry;
    }
}
