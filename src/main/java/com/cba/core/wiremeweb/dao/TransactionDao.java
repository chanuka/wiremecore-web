package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.TransactionCore;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionDao {

    Page<TransactionCore> getAllTransactions(String dateFrom, String dateTo, int page, int pageSize) throws Exception;

    List<Object[]> getAllTransactionSummary(String selectClause,String whereClause ,String groupByClause,
                                            String dateFrom, String dateTo) throws Exception;

}
