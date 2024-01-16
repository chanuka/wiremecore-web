package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.TransactionDao;
import com.cba.core.wiremeweb.model.TransactionCore;
import com.cba.core.wiremeweb.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class TransactionDaoImpl implements TransactionDao {

    private final TransactionRepository transactionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<TransactionCore> getAllTransactions(String dateFrom, String dateTo, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return transactionRepository.findByDateTimeBetween(dateFormat.parse(dateFrom), dateFormat.parse(dateTo), pageable);

    }

    @Override
    public List<Object[]> getAllTransactionSummary(String selectClause,
                                                   String whereClause,
                                                   String groupByClause,
                                                   String dateFrom,
                                                   String dateTo) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String jpql = "SELECT " + selectClause + " FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
                " WHERE " + whereClause + " GROUP BY " + groupByClause;

        Query query = entityManager.createQuery(jpql);

        if ((dateFrom != null && !dateFrom.isEmpty())
                && (dateTo != null && !dateTo.isEmpty())) {
            query.setParameter("fromDate", dateFormat.parse(dateFrom));
            query.setParameter("toDate", dateFormat.parse(dateTo));
        } else {
        }

        return query.getResultList();

    }

}
