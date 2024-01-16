package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GraphDao;
import com.cba.core.wiremeweb.dto.GraphRequestDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
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

@Repository
@RequiredArgsConstructor
public class GraphDaoImpl implements GraphDao {

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
    public UserConfig deleteByUser_NameAndConfigType(String userName, String configName) throws Exception {
        repository.deleteByUser_NameAndConfigName(userName, configName);
        return new UserConfig();
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
    public List<Object[]> findGraphs(String whereClause,
                                     String selectClause,
                                     String groupByClause,
                                     GraphRequestDto requestDto) throws Exception {
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
            } else {
                throw new NotFoundException("No Record Found");
            }

            return query.getResultList();

        } catch (ParseException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
