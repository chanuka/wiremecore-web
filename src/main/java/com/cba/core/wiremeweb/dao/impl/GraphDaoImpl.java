package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GraphDao;
import com.cba.core.wiremeweb.dto.GraphRequestDto;
import com.cba.core.wiremeweb.dto.GraphResponseDto;
import com.cba.core.wiremeweb.dto.HighlightRequestDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.GraphMapper;
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
import java.util.*;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class GraphDaoImpl implements GraphDao {

    private final DashBoardRepository repository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final UserBean userBean;
    private final ObjectMapper objectMapper;

    @Value("${application.resource.users}")
    private String resource;

    @Override
    public List<GraphResponseDto> findAll(String configType) throws Exception {

        List<UserConfig> entityList = repository.findByUser_NameAndConfigType(userBean.getUsername(), configType);
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
        try {

            UserConfig entity = repository.findByUser_NameAndConfigName(userBean.getUsername(), configName)
                    .orElseThrow(() -> new NotFoundException("User Config not found"));

            GraphResponseDto responseDto = objectMapper.readValue(entity.getConfig(), GraphResponseDto.class);

            repository.deleteByUser_NameAndConfigName(userBean.getUsername(), configName);

            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                    entity.getUser().getId(), entity.getConfig(), null,
                    userBean.getRemoteAdr()));

            return GraphMapper.toDto(responseDto, entity);

        } catch (Exception rr) {
            rr.printStackTrace();
            throw rr;
        }
    }

    @Override
    public GraphResponseDto create(GraphRequestDto requestDto) throws Exception {

        String config = objectMapper.writeValueAsString(requestDto);
        UserConfig toInsert = GraphMapper.toModel(requestDto, config, userRepository.findByUserName(userBean.getUsername()));

        UserConfig savedEntity = repository.save(toInsert);

        GraphResponseDto responseDto = objectMapper.readValue(savedEntity.getConfig(), GraphResponseDto.class);

        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBean.getRemoteAdr()));

        return responseDto;
    }

    @Override
    public GraphResponseDto update(String configName, GraphRequestDto requestDto) throws Exception {

        UserConfig toBeUpdatedEntity = repository.findByUser_NameAndConfigName(userBean.getUsername(), configName).orElseThrow(() -> new NotFoundException("User Config not found"));
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
            repository.saveAndFlush(toBeUpdatedEntity);
            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                    toBeUpdatedEntity.getId(), objectMapper.writeValueAsString(oldDataMap), objectMapper.writeValueAsString(newDataMap),
                    userBean.getRemoteAdr()));

            return GraphMapper.toDto(toBeUpdatedDto, toBeUpdatedEntity);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    public Map<String, Object> findGraphs(GraphRequestDto requestDto) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> responseData = new HashMap<>();

        try {
            Date fromDate = dateFormat.parse(requestDto.getFromDate());
            Date toDate = dateFormat.parse(requestDto.getToDate());

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("metaData", requestDto);
            responseData.put("0", metadata);

            if ("Revenue".equals(requestDto.getYaxis())) {
                if ("CardLabel".equals(requestDto.getGrouping())) {
                    List<Object[]> revenues = transactionRepository.revenueTransactionCoreGroupByCardLabelAndDistrict(fromDate, toDate);
//                    for (int i = 0; i < revenues.size(); i++) {
//                        ArrayList<Map<String, Object>> list = null;
//                        if (!responseData.containsKey((String) revenues.get(i)[0])) {
//                            list = new ArrayList<>();
//                        } else {
//                            list = (ArrayList<Map<String, Object>>) responseData.get((String) revenues.get(i)[0]);
//                        }
//                        Map<String, Object> map1 = new HashMap<>();
//                        map1.put("grouping", (String) revenues.get(i)[1]);
//                        map1.put("value", (Long) revenues.get(i)[2]);
//                        list.add(map1);
//                        responseData.put(((String) revenues.get(i)[0]), list);
//                    }
                    Map<String, List<Map<String, Object>>> responseData2 = new HashMap<>();
                    revenues.forEach(count -> {
                        String key = (String) count[0];
                        String grouping = (String) count[1];
                        Long value = (Long) count[2];

                        responseData2.computeIfAbsent(key, k -> new ArrayList<>())
                                .add(Map.of("grouping", grouping, "value", value));
                    });
                    responseData.putAll(responseData2);
                } else if ("PaymentMode".equals(requestDto.getGrouping())) {
                    List<Object[]> revenues = transactionRepository.revenueTransactionCoreGroupByPaymentModeAndDistrict(fromDate, toDate);
//                    for (int i = 0; i < revenues.size(); i++) {
//                        ArrayList<Map<String, Object>> list = null;
//                        if (!responseData.containsKey((String) revenues.get(i)[0])) {
//                            list = new ArrayList<>();
//                        } else {
//                            list = (ArrayList<Map<String, Object>>) responseData.get((String) revenues.get(i)[0]);
//                        }
//                        Map<String, Object> map1 = new HashMap<>();
//                        map1.put("grouping", (String) revenues.get(i)[1]);
//                        map1.put("value", (Long) revenues.get(i)[2]);
//                        list.add(map1);
//                        responseData.put(((String) revenues.get(i)[0]), list);
//                    }
                    Map<String, List<Map<String, Object>>> responseData2 = new HashMap<>();
                    revenues.forEach(count -> {
                        String key = (String) count[0];
                        String grouping = (String) count[1];
                        Long value = (Long) count[2];

                        responseData2.computeIfAbsent(key, k -> new ArrayList<>())
                                .add(Map.of("grouping", grouping, "value", value));
                    });
                    responseData.putAll(responseData2);
                } else if ("TranType".equals(requestDto.getGrouping())) {
                    List<Object[]> revenues = transactionRepository.revenueTransactionCoreGroupByTranTypeAndDistrict(fromDate, toDate);
//                    for (int i = 0; i < revenues.size(); i++) {
//                        ArrayList<Map<String, Object>> list = null;
//                        if (!responseData.containsKey((String) revenues.get(i)[0])) {
//                            list = new ArrayList<>();
//                        } else {
//                            list = (ArrayList<Map<String, Object>>) responseData.get((String) revenues.get(i)[0]);
//                        }
//                        Map<String, Object> map1 = new HashMap<>();
//                        map1.put("grouping", (String) revenues.get(i)[1]);
//                        map1.put("value", (Long) revenues.get(i)[2]);
//                        list.add(map1);
//                        responseData.put(((String) revenues.get(i)[0]), list);
//                    }
                    Map<String, List<Map<String, Object>>> responseData2 = new HashMap<>();
                    revenues.forEach(count -> {
                        String key = (String) count[0];
                        String grouping = (String) count[1];
                        Long value = (Long) count[2];

                        responseData2.computeIfAbsent(key, k -> new ArrayList<>())
                                .add(Map.of("grouping", grouping, "value", value));
                    });
                    responseData.putAll(responseData2);
                } else {
                    return null;
                }
            } else if ("Count".equals(requestDto.getYaxis())) {
                if ("CardLabel".equals(requestDto.getGrouping())) {
                    List<Object[]> counts = transactionRepository.countTransactionCoreGroupByCardLabelAndDistrict(fromDate, toDate);
//                    for (int i = 0; i < counts.size(); i++) {
//                        ArrayList<Map<String, Object>> list = null;
//                        if (!responseData.containsKey((String) counts.get(i)[0])) {
//                            list = new ArrayList<>();
//                        } else {
//                            list = (ArrayList<Map<String, Object>>) responseData.get((String) counts.get(i)[0]);
//                        }
//                        Map<String, Object> map1 = new HashMap<>();
//                        map1.put("grouping", (String) counts.get(i)[1]);
//                        map1.put("value", (Long) counts.get(i)[2]);
//                        list.add(map1);
//                        responseData.put(((String) counts.get(i)[0]), list);
//                    }
                    Map<String, List<Map<String, Object>>> responseData2 = new HashMap<>();
                    counts.forEach(count -> {
                        String key = (String) count[0];
                        String grouping = (String) count[1];
                        Long value = (Long) count[2];

                        responseData2.computeIfAbsent(key, k -> new ArrayList<>())
                                .add(Map.of("grouping", grouping, "value", value));
                    });
                    responseData.putAll(responseData2);

                } else if ("PaymentMode".equals(requestDto.getGrouping())) {
                    List<Object[]> counts = transactionRepository.countTransactionCoreGroupByPaymentModeAndDistrict(fromDate, toDate);
//                    for (int i = 0; i < counts.size(); i++) {
//                        ArrayList<Map<String, Object>> list = null;
//                        if (!responseData.containsKey((String) counts.get(i)[0])) {
//                            list = new ArrayList<>();
//                        } else {
//                            list = (ArrayList<Map<String, Object>>) responseData.get((String) counts.get(i)[0]);
//                        }
//                        Map<String, Object> map1 = new HashMap<>();
//                        map1.put("grouping", (String) counts.get(i)[1]);
//                        map1.put("value", (Long) counts.get(i)[2]);
//                        list.add(map1);
//                        responseData.put(((String) counts.get(i)[0]), list);
//                    }
                    Map<String, List<Map<String, Object>>> responseData2 = new HashMap<>();
                    counts.forEach(count -> {
                        String key = (String) count[0];
                        String grouping = (String) count[1];
                        Long value = (Long) count[2];

                        responseData2.computeIfAbsent(key, k -> new ArrayList<>())
                                .add(Map.of("grouping", grouping, "value", value));
                    });
                    responseData.putAll(responseData2);
                } else if ("TranType".equals(requestDto.getGrouping())) {
                    List<Object[]> counts = transactionRepository.countTransactionCoreGroupByTranTypeAndDistrict(fromDate, toDate);
//                    for (int i = 0; i < counts.size(); i++) {
//                        ArrayList<Map<String, Object>> list = null;
//                        if (!responseData.containsKey((String) counts.get(i)[0])) {
//                            list = new ArrayList<>();
//                        } else {
//                            list = (ArrayList<Map<String, Object>>) responseData.get((String) counts.get(i)[0]);
//                        }
//                        Map<String, Object> map1 = new HashMap<>();
//                        map1.put("grouping", (String) counts.get(i)[1]);
//                        map1.put("value", (Long) counts.get(i)[2]);
//                        list.add(map1);
//                        responseData.put(((String) counts.get(i)[0]), list);
//                    }
                    Map<String, List<Map<String, Object>>> responseData2 = new HashMap<>();
                    counts.forEach(count -> {
                        String key = (String) count[0];
                        String grouping = (String) count[1];
                        Long value = (Long) count[2];

                        responseData2.computeIfAbsent(key, k -> new ArrayList<>())
                                .add(Map.of("grouping", grouping, "value", value));
                    });
                    responseData.putAll(responseData2);
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

    private Map<String, Object> createDataEntry(GraphRequestDto requestDto, long count, String cardLabel) {
        Map<String, Object> dataEntry = new HashMap<>();
        dataEntry.put(requestDto.getAggregator(), count);
        dataEntry.put(requestDto.getGrouping(), cardLabel);
        return dataEntry;
    }
}
