package com.cba.core.wiremeweb.repository.specification;

import com.cba.core.wiremeweb.model.Device;
import com.cba.core.wiremeweb.model.TransactionCore;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public interface TransactionSpecification {

    static Specification<TransactionCore> fromDateAndToDate(Date fromDate, Date toDate) throws Exception {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.between(root.get("dateTime"), fromDate, toDate),
                        criteriaBuilder.between(root.get("dateTime"), fromDate, toDate)
                );
    }

}
