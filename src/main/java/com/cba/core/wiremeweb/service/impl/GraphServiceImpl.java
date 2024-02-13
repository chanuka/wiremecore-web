package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.GlobalAuditDao;
import com.cba.core.wiremeweb.dao.GraphDao;
import com.cba.core.wiremeweb.dao.UserDao;
import com.cba.core.wiremeweb.dto.GraphRequestDto;
import com.cba.core.wiremeweb.dto.GraphResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.GraphMapper;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.model.User;
import com.cba.core.wiremeweb.model.UserConfig;
import com.cba.core.wiremeweb.service.GraphService;
import com.cba.core.wiremeweb.util.UserBeanUtil;
import com.cba.core.wiremeweb.util.UserOperationEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GraphServiceImpl implements GraphService {

    private final GraphDao dao;
    private final GlobalAuditDao globalAuditDao;
    private final UserDao<User> userDao;
    private final UserBeanUtil userBeanUtil;
    private final ObjectMapper objectMapper;

    @Value("${application.resource.users}")
    private String resource;

    @Override
    public List<GraphResponseDto> findAll(String configType) throws Exception {
        List<UserConfig> entityList = dao.findAll(userBeanUtil.getUsername(), configType);
        if (entityList.isEmpty()) {
            throw new NotFoundException("No User Config found");
        }

        return entityList
                .stream()
                .map((userConfig -> {
                    try {
                        GraphResponseDto responseDto = objectMapper.readValue(userConfig.getConfig(), GraphResponseDto.class);
                        return GraphMapper.toDto(responseDto, userConfig);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }))
                .collect(Collectors.toList());
    }

    @Override
    public GraphResponseDto deleteByUser_NameAndConfigType(String configName) throws Exception {
        UserConfig entity = dao.findByUser_NameAndConfigName(userBeanUtil.getUsername(), configName);

        GraphResponseDto responseDto = objectMapper.readValue(entity.getConfig(), GraphResponseDto.class);
        dao.deleteByUser_NameAndConfigType(userBeanUtil.getUsername(), configName);

        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                entity.getUser().getId(), entity.getConfig(), null,
                userBeanUtil.getRemoteAdr()));

        return GraphMapper.toDto(responseDto, entity);
    }

    @Override
    public GraphResponseDto create(GraphRequestDto requestDto) throws Exception {
        String config = objectMapper.writeValueAsString(requestDto);
        User user = userDao.findByUserName(userBeanUtil.getUsername());
        UserConfig toInsert = GraphMapper.toModel(requestDto, config, user);

        UserConfig savedEntity = dao.create(toInsert);

        GraphResponseDto responseDto = objectMapper.readValue(savedEntity.getConfig(), GraphResponseDto.class);

        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    public GraphResponseDto update(String configName, GraphRequestDto requestDto) throws Exception {
        UserConfig toBeUpdatedEntity = dao.findByUser_NameAndConfigName(userBeanUtil.getUsername(), configName);
        GraphResponseDto toBeUpdatedDto = objectMapper.readValue(toBeUpdatedEntity.getConfig(), GraphResponseDto.class);

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
            if (toBeUpdatedDto.getAggregator() != null && !toBeUpdatedDto.getAggregator().equals(requestDto.getAggregator())
                    || requestDto.getAggregator() != null && !requestDto.getAggregator().equals(toBeUpdatedDto.getAggregator())) {
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
            if (toBeUpdatedDto.getXaxis() != null && !toBeUpdatedDto.getXaxis().equals(requestDto.getXaxis())
                    || requestDto.getXaxis() != null && !requestDto.getXaxis().equals(toBeUpdatedDto.getXaxis())) {
                updateRequired = true;
                oldDataMap.put("xaxis", toBeUpdatedDto.getXaxis());
                newDataMap.put("xaxis", requestDto.getXaxis());

                toBeUpdatedDto.setXaxis(requestDto.getXaxis());
            }
            if (toBeUpdatedDto.getYaxis() != null && !toBeUpdatedDto.getYaxis().equals(requestDto.getYaxis())
                    || requestDto.getYaxis() != null && !requestDto.getYaxis().equals(toBeUpdatedDto.getYaxis())) {
                updateRequired = true;
                oldDataMap.put("yaxis", toBeUpdatedDto.getYaxis());
                newDataMap.put("yaxis", requestDto.getYaxis());

                toBeUpdatedDto.setYaxis(requestDto.getYaxis());
            }
            if (!toBeUpdatedDto.getGraphType().equals(requestDto.getGraphType())) {
                updateRequired = true;
                oldDataMap.put("graphType", toBeUpdatedDto.getGraphType());
                newDataMap.put("graphType", requestDto.getGraphType());

                toBeUpdatedDto.setGraphType(requestDto.getGraphType());
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

            return GraphMapper.toDto(toBeUpdatedDto, toBeUpdatedEntity);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    public Map<String, Object> findGraphs(GraphRequestDto requestDto) throws Exception {

        Map<String, Object> responseData = new HashMap<>();
        try {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("metaData", requestDto);
            responseData.put("0", metadata);

            String whereClause = setWhereCondition(requestDto);
            String selectClause = setSelectCondition(requestDto);
            String groupByClause = setGroupByCondition(requestDto);

            List<Object[]> list = dao.findGraphs(whereClause, selectClause, groupByClause, requestDto);
            responseData.putAll(getStringListMap(list));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return responseData;
    }

    private String setWhereCondition(GraphRequestDto requestDto) throws Exception {

        String fromDate = requestDto.getFromDate();
        String toDate = requestDto.getToDate();
        String partner = requestDto.getSelectionScope().getPartner();
        String merchant = requestDto.getSelectionScope().getMerchant();
        String province = requestDto.getSelectionScope().getProvince();
        String district = requestDto.getSelectionScope().getDistrict();

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
                && (requestDto.getToDate() != null && !toDate.isEmpty())) {
            where += " AND p.dateTime BETWEEN :fromDate AND :toDate ";
        } else {
            throw new NotFoundException("No Record Found");
        }

        return where;
    }

    private String setSelectCondition(GraphRequestDto requestDto) throws Exception {

        String aggregator = requestDto.getYaxis();
        String grouping = requestDto.getGrouping();
        String xAxis = requestDto.getXaxis();

        String select = " ";

        if ((aggregator != null && !"".equals(aggregator)) && (aggregator != null && !"".equals(aggregator))) {
            if ("CardLabel".equalsIgnoreCase(grouping)) {
                if ("Revenue".equalsIgnoreCase(aggregator)) {
                    if ("Districts".equalsIgnoreCase(xAxis)) {
                        select = " m.district ";
                    } else if ("Provinces".equalsIgnoreCase(xAxis)) {
                        select = " m.province ";
                    } else if ("Merchants".equalsIgnoreCase(xAxis)) {
                        select = " m.merchantId ";
                    } else if ("Partners".equalsIgnoreCase(xAxis)) {
                        select = " m.merchantCustomer.name ";
                    } else {

                    }
                    select += " ,p.cardLabel,sum(p.amount) ";
                }
                if ("Count".equalsIgnoreCase(aggregator)) {
                    if ("Districts".equalsIgnoreCase(xAxis)) {
                        select += " m.district ";
                    } else if ("Provinces".equalsIgnoreCase(xAxis)) {
                        select += " m.province ";
                    } else if ("Merchants".equalsIgnoreCase(xAxis)) {
                        select += " m.merchantId ";
                    } else if ("Partners".equalsIgnoreCase(xAxis)) {
                        select += " m.merchantCustomer.name ";
                    } else {

                    }
                    select += " ,p.cardLabel,count(p) ";
                }
            }
            if ("PaymentMode".equalsIgnoreCase(grouping)) {
                if ("Revenue".equalsIgnoreCase(aggregator)) {
                    if ("Districts".equalsIgnoreCase(xAxis)) {
                        select += " m.district ";
                    } else if ("Provinces".equalsIgnoreCase(xAxis)) {
                        select += " m.province ";
                    } else if ("Merchants".equalsIgnoreCase(xAxis)) {
                        select += " m.merchantId ";
                    } else if ("Partners".equalsIgnoreCase(xAxis)) {
                        select += " m.merchantCustomer.name ";
                    } else {

                    }
                    select += " ,p.paymentMode,sum(p.amount) ";
                }
                if ("Count".equalsIgnoreCase(aggregator)) {
                    if ("Districts".equalsIgnoreCase(xAxis)) {
                        select += " m.district ";
                    } else if ("Provinces".equalsIgnoreCase(xAxis)) {
                        select += " m.province ";
                    } else if ("Merchants".equalsIgnoreCase(xAxis)) {
                        select += " m.merchantId ";
                    } else if ("Partners".equalsIgnoreCase(xAxis)) {
                        select += " m.merchantCustomer.name ";
                    } else {

                    }
                    select += " ,p.paymentMode,count(p) ";
                }
            }
            if ("TranType".equalsIgnoreCase(grouping)) {
                if ("Revenue".equalsIgnoreCase(aggregator)) {
                    if ("Districts".equalsIgnoreCase(xAxis)) {
                        select += " m.district, ";
                    } else if ("Provinces".equalsIgnoreCase(xAxis)) {
                        select += " m.province, ";
                    } else if ("Merchants".equalsIgnoreCase(xAxis)) {
                        select += " m.merchantId, ";
                    } else if ("Partners".equalsIgnoreCase(xAxis)) {
                        select += " m.merchantCustomer.name, ";
                    } else {

                    }
                    select += " ,p.tranType,sum(p.amount) ";
                }
                if ("Count".equalsIgnoreCase(aggregator)) {
                    if ("Districts".equalsIgnoreCase(xAxis)) {
                        select += " m.district ";
                    } else if ("Provinces".equalsIgnoreCase(xAxis)) {
                        select += " m.province ";
                    } else if ("Merchants".equalsIgnoreCase(xAxis)) {
                        select += " m.merchantId ";
                    } else if ("Partners".equalsIgnoreCase(xAxis)) {
                        select += " m.merchantCustomer.name ";
                    } else {

                    }
                    select += " ,p.tranType,count(p) ";
                }
            }

        } else {
        }

        return select;
    }

    private String setGroupByCondition(GraphRequestDto requestDto) throws Exception {

        String grouping = requestDto.getGrouping();
        String xAxis = requestDto.getXaxis();
        String groupBy = " ";
        if (grouping != null && !"".equals(grouping)) {
            if ("CardLabel".equalsIgnoreCase(grouping)) {
                if ("Districts".equalsIgnoreCase(xAxis)) {
                    groupBy = " m.district ";
                } else if ("Provinces".equalsIgnoreCase(xAxis)) {
                    groupBy = " m.province ";
                } else if ("Merchants".equalsIgnoreCase(xAxis)) {
                    groupBy = " m.merchantId";
                } else if ("Partners".equalsIgnoreCase(xAxis)) {
                    groupBy = " m.merchantCustomer.name ";
                } else {
                    throw new NotFoundException("No Record Found");
                }
                groupBy += ",p.cardLabel";
            }
            if ("PaymentMode".equalsIgnoreCase(grouping)) {
                if ("Districts".equalsIgnoreCase(xAxis)) {
                    groupBy += " m.district ";
                } else if ("Provinces".equalsIgnoreCase(xAxis)) {
                    groupBy += " m.province ";
                } else if ("Merchants".equalsIgnoreCase(xAxis)) {
                    groupBy += " m.merchantId ";
                } else if ("Partners".equalsIgnoreCase(xAxis)) {
                    groupBy += " m.merchantCustomer.name ";
                } else {
                    throw new NotFoundException("No Record Found");
                }
                groupBy += ",p.paymentMode";

            }
            if ("TranType".equalsIgnoreCase(grouping)) {
                if ("Districts".equalsIgnoreCase(xAxis)) {
                    groupBy += " m.district ";
                } else if ("Provinces".equalsIgnoreCase(xAxis)) {
                    groupBy += " m.province ";
                } else if ("Merchants".equalsIgnoreCase(xAxis)) {
                    groupBy += " m.merchantId ";
                } else if ("Partners".equalsIgnoreCase(xAxis)) {
                    groupBy += " m.merchantCustomer.name ";
                } else {
                    throw new NotFoundException("No Record Found");
                }
                groupBy += ",p.tranType";
            }
        } else {
            throw new NotFoundException("No Record Found");
        }

        return groupBy;
    }

    private Map<String, List<Map<String, Object>>> getStringListMap(List<Object[]> list) {
        Map<String, List<Map<String, Object>>> responseData = new HashMap<>();
        list.forEach(obj -> {
            String key = (String) obj[0];
            String grouping = (String) obj[1];
            Long value = (Long) obj[2];

            responseData.computeIfAbsent(key, k -> new ArrayList<>())
                    .add(Map.of("grouping", grouping, "value", value));
        });
        return responseData;
    }
}
