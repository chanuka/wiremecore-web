package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dao.MerchantDao;
import com.cba.core.wiremeweb.dao.TerminalDao;
import com.cba.core.wiremeweb.dao.TransactionDao;
import com.cba.core.wiremeweb.dto.MerchantCustomerResponseDto;
import com.cba.core.wiremeweb.dto.MerchantResponseDto;
import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import com.cba.core.wiremeweb.dto.TransactionCoreResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.MerchantCustomerMapper;
import com.cba.core.wiremeweb.mapper.MerchantMapper;
import com.cba.core.wiremeweb.mapper.TerminalMapper;
import com.cba.core.wiremeweb.mapper.TransactionCoreMapper;
import com.cba.core.wiremeweb.model.Merchant;
import com.cba.core.wiremeweb.model.MerchantCustomer;
import com.cba.core.wiremeweb.model.Terminal;
import com.cba.core.wiremeweb.model.TransactionCore;
import com.cba.core.wiremeweb.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionDao;
    private final TerminalDao<Terminal> terminalDao;
    private final MerchantDao<Merchant> merchantDao;
    private final GenericDao<MerchantCustomer> merchantCustomerDao;


    @Value("${application.resource.transactions}")
    private String resource;

    @Override
    public List<TerminalResponseDto> findAllTerminals() throws Exception {
        List<Terminal> entityList = terminalDao.findAll();
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
        List<Merchant> entityList = merchantDao.findAll();
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
        List<MerchantCustomer> entityList = merchantCustomerDao.findAll();
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
        Page<TransactionCore> entitiesPage = transactionDao.getAllTransactions(dateFrom, dateTo, page, pageSize);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Transactions found");
        }
        return entitiesPage.map(TransactionCoreMapper::toDto);
    }

    @Override
    public Map<String, ArrayList<Map<String, Object>>> getAllTransactionSummary(String dateFrom, String dateTo, String queryBy) throws Exception {

        ArrayList<Map<String, Object>> countList = new ArrayList();
        ArrayList<Map<String, Object>> amountList = new ArrayList();

        String whereClause = setWhereCondition(dateFrom, dateTo);
        String selectClause = setSelectCondition(queryBy);
        String groupByClause = setGroupByCondition(queryBy);

        List<Object[]> list = transactionDao.getAllTransactionSummary(selectClause, whereClause, groupByClause,
                dateFrom, dateTo);


        list.forEach(i -> {
            Map<String, Object> countMap = new HashMap<>();
            Map<String, Object> amountMap = new HashMap<>();
            String label = (String) i[0];
            Long count = (Long) i[1];
            BigDecimal amount = (BigDecimal) i[2];

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
