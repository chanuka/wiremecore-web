package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.HighlightDao;
import com.cba.core.wiremeweb.dto.HighlightRequestDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.model.TransactionCore;
import com.cba.core.wiremeweb.model.UserConfig;
import com.cba.core.wiremeweb.repository.DashBoardRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class HighlightDaoImpl implements HighlightDao {

    private final DashBoardRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserConfig> findAll(String userName, String configType) throws Exception {
        return repository.findByUser_NameAndConfigType(userName, configType);
    }

    @Override
    public UserConfig findByUser_NameAndConfigName(String userName, String configName) throws Exception {
        return repository.findByUser_NameAndConfigName(userName, configName)
                .orElseThrow(() -> new NotFoundException("User Config not found"));
    }

    @Override
    public void deleteByUser_NameAndConfigName(String userName, String configName) throws Exception {
        repository.deleteByUser_NameAndConfigName(userName, configName);
    }

    @Override
    public UserConfig create(UserConfig toInsert) throws Exception {
        return repository.save(toInsert);
    }

    @Override
    public UserConfig update(String configName, UserConfig toBeUpdatedEntity) throws Exception {
        return repository.saveAndFlush(toBeUpdatedEntity);
    }

    @Override
    public List<Object[]> findHighLights(String whereClause, String selectClause, String groupByClause,
                                         HighlightRequestDto requestDto) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {

            String fromDate = requestDto.getFromDate();
            String toDate = requestDto.getToDate();
            String partner = requestDto.getSelectionScope().getPartner();
            String merchant = requestDto.getSelectionScope().getMerchant();
            String province = requestDto.getSelectionScope().getProvince();
            String district = requestDto.getSelectionScope().getDistrict();

            String jpql = "SELECT " + selectClause + " FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
                    "WHERE " + whereClause + " GROUP BY " + groupByClause;

            Query query = entityManager.createQuery(jpql);

            if (partner != null && !"all".equalsIgnoreCase(partner)) {
                query.setParameter("partner", partner);
            }
            if (merchant != null && !"all".equalsIgnoreCase(merchant)) {
                query.setParameter("merchant", merchant);
            }
            if (province != null && !"all".equalsIgnoreCase(province)) {
                query.setParameter("province", province);
            }
            if (district != null && !"all".equalsIgnoreCase(district)) {
                query.setParameter("district", district);
            }
            if ((fromDate != null && !fromDate.isEmpty())
                    && (toDate != null && !toDate.isEmpty())) {
                query.setParameter("fromDate", dateFormat.parse(fromDate));
                query.setParameter("toDate", dateFormat.parse(toDate));
            } else {
            }

            return query.getResultList();

        } catch (ParseException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<TransactionCore> findHighLightsDetail(String whereClause,HighlightRequestDto requestDto) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            String filterKey = "", filterValue = "";
            if (requestDto.getFilter() != null) {
                for (Map.Entry<String, String> entry : requestDto.getFilter().entrySet()) {
                    filterKey = entry.getKey();
                    filterValue = entry.getValue();
                }
            }
            String fromDate = requestDto.getFromDate();
            String toDate = requestDto.getToDate();
            String partner = requestDto.getSelectionScope().getPartner();
            String merchant = requestDto.getSelectionScope().getMerchant();
            String province = requestDto.getSelectionScope().getProvince();
            String district = requestDto.getSelectionScope().getDistrict();

            String jpql = "SELECT p FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
                    "WHERE " + whereClause;

            Query query = entityManager.createQuery(jpql);

            if (partner != null && !"all".equals(partner)) {
                query.setParameter("partner", partner);
            }
            if (merchant != null && !"all".equals(merchant)) {
                query.setParameter("merchant", merchant);
            }
            if (province != null && !"all".equals(province)) {
                query.setParameter("province", province);
            }
            if (district != null && !"all".equals(district)) {
                query.setParameter("district", district);
            }
            if ((fromDate != null && !fromDate.isEmpty())
                    && (requestDto.getToDate() != null && !toDate.isEmpty())) {
                query.setParameter("fromDate", dateFormat.parse(fromDate));
                query.setParameter("toDate", dateFormat.parse(toDate));
            }
            if (filterKey != null && !"".equals(filterKey) && filterValue != null && !"".equals(filterValue)) {
                query.setParameter("filterValue", filterValue);
            } else {
            }

            return query.getResultList();

        } catch (ParseException e) {
            e.printStackTrace();
            throw e;
        }
    }


}
