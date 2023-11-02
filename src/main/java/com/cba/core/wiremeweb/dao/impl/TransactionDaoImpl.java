package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.TransactionDao;
import com.cba.core.wiremeweb.dto.*;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.MerchantCustomerMapper;
import com.cba.core.wiremeweb.mapper.MerchantMapper;
import com.cba.core.wiremeweb.mapper.TerminalMapper;
import com.cba.core.wiremeweb.mapper.TransactionCoreMapper;
import com.cba.core.wiremeweb.model.Merchant;
import com.cba.core.wiremeweb.model.MerchantCustomer;
import com.cba.core.wiremeweb.model.Terminal;
import com.cba.core.wiremeweb.model.TransactionCore;
import com.cba.core.wiremeweb.repository.MerchantCustomerRepository;
import com.cba.core.wiremeweb.repository.MerchantRepository;
import com.cba.core.wiremeweb.repository.TerminalRepository;
import com.cba.core.wiremeweb.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class TransactionDaoImpl implements TransactionDao {

    private final TerminalRepository terminalRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantCustomerRepository merchantCustomerRepository;
    private final TransactionRepository transactionRepository;

    @Value("${application.resource.transactions}")
    private String resource;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TerminalResponseDto> findAllTerminals() throws Exception {
        List<Terminal> entityList = terminalRepository.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Terminals found");
        }
        return entityList
                .stream()
                .map(TerminalMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MerchantResponseDto> getAllMerchants() throws Exception {
        List<Merchant> entityList = merchantRepository.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Merchants found");
        }
        return entityList
                .stream()
                .map(MerchantMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MerchantCustomerResponseDto> getAllMerchantCustomers() throws Exception {
        List<MerchantCustomer> entityList = merchantCustomerRepository.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Merchant Customers found");
        }
        return entityList
                .stream()
                .map(MerchantCustomerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TransactionCoreResponseDto> getAllTransactions(String dateFrom, String dateTo, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Page<TransactionCore> entitiesPage = transactionRepository.findByDateTimeBetween(dateFormat.parse(dateFrom), dateFormat.parse(dateTo),
                pageable);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Transactions found");
        }
        return entitiesPage.map(TransactionCoreMapper::toDto);
    }

    @Override
    public Map<String, ArrayList<Map<String, Object>>> getAllTransactionSummary(String dateFrom, String dateTo, String queryBy) throws Exception {

        ArrayList<Map<String, Object>> countList = new ArrayList();
        ArrayList<Map<String, Object>> amountList = new ArrayList();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String whereClause = setWhereCondition(dateFrom, dateTo);
        String selectClause = setSelectCondition(queryBy);
        String groupByClause = setGroupByCondition(queryBy);


        String jpql = "SELECT " + selectClause + " FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
                " WHERE " + whereClause + " GROUP BY " + groupByClause;

        Query query = entityManager.createQuery(jpql);

        if ((dateFrom != null && !dateFrom.isEmpty())
                && (dateTo != null && !dateTo.isEmpty())) {
            query.setParameter("fromDate", dateFormat.parse(dateFrom));
            query.setParameter("toDate", dateFormat.parse(dateTo));
        } else {
        }

        List<Object[]> list = query.getResultList();

        list.forEach(i -> {
            Map<String, Object> countMap = new HashMap<>();
            Map<String, Object> amountMap = new HashMap<>();
            String label = (String) i[0];
            Long count = (Long) i[1];
            Long amount = (Long) i[2];
            countMap.put("count", count);
            if ((queryBy != null && !"".equals(queryBy))) {
                if ("CardLabel".equalsIgnoreCase(queryBy)) {
                    countMap.put("cardLabel", label);
                }
                if ("PaymentMode".equalsIgnoreCase(queryBy)) {
                    countMap.put("paymentMode", label);
                }
            }
            amountMap.put("total", amount);
            if ((queryBy != null && !"".equals(queryBy))) {
                if ("CardLabel".equalsIgnoreCase(queryBy)) {
                    amountMap.put("cardLabel", label);
                }
                if ("PaymentMode".equalsIgnoreCase(queryBy)) {
                    amountMap.put("paymentMode", label);
                }
            }
            countList.add(countMap);
            amountList.add(amountMap);
        });


        Map<String, ArrayList<Map<String, Object>>> returnMap = new HashMap<>();
        returnMap.put("totalCount", countList);
        returnMap.put("totalAmount", amountList);

        return returnMap;
    }

    private String setGroupByCondition(String queryBy) throws Exception {

        String groupBy = " ";
        if (queryBy != null && !"".equals(queryBy)) {
            if ("CardLabel".equalsIgnoreCase(queryBy)) {
                groupBy += " p.cardLabel";
            }
            if ("PaymentMode".equalsIgnoreCase(queryBy)) {
                groupBy += " p.paymentMode";
            }

        } else {
        }

        return groupBy;
    }

    private String setSelectCondition(String queryBy) throws Exception {

        String select = " ";

        if ((queryBy != null && !"".equals(queryBy))) {
            if ("CardLabel".equalsIgnoreCase(queryBy)) {
                select += " p.cardLabel,count(p) ,sum(p.amount) ";
            }
            if ("PaymentMode".equalsIgnoreCase(queryBy)) {
                select += " p.paymentMode,count(p),sum(p.amount) ";
            }
        } else {
        }
        return select;
    }

    private String setWhereCondition(String dateFrom, String dateTo) throws Exception {

        String where = " 1=1 ";

        if ((dateFrom != null && !dateFrom.isEmpty())
                && (dateTo != null && !dateTo.isEmpty())) {
            where += " AND p.dateTime BETWEEN :fromDate AND :toDate ";
        } else {
        }
        return where;
    }

}
