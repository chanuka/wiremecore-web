package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.TransactionCore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionCore, Integer>, JpaSpecificationExecutor<TransactionCore> {

    @Query("SELECT p.cardLabel, COUNT(p) FROM TransactionCore p WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.cardLabel")
    List<Object[]> countTransactionCoreGroupByCardLabel(Date fromDate, Date toDate);

    @Query("SELECT p.cardLabel, sum(p.amount) FROM TransactionCore p WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.cardLabel")
    List<Object[]> revenueTransactionCoreGroupByCardLabel(Date fromDate, Date toDate);

    @Query("SELECT p.paymentMode, COUNT(p) FROM TransactionCore p WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.paymentMode")
    List<Object[]> countTransactionCoreGroupByPaymentMode(Date fromDate, Date toDate);

    @Query("SELECT p.paymentMode, sum(p.amount) FROM TransactionCore p WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.paymentMode")
    List<Object[]> revenueTransactionCoreGroupByPaymentMode(Date fromDate, Date toDate);

    @Query("SELECT p.tranType, COUNT(p) FROM TransactionCore p WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.tranType")
    List<Object[]> countTransactionCoreGroupByTranType(Date fromDate, Date toDate);

    @Query("SELECT p.tranType, sum(p.amount) FROM TransactionCore p WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.tranType")
    List<Object[]> revenueTransactionCoreGroupByTranType(Date fromDate, Date toDate);

    @Query("SELECT m.district,p.cardLabel, COUNT(p) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.cardLabel,m.district")
    List<Object[]> countTransactionCoreGroupByCardLabelAndDistrict(Date fromDate, Date toDate);

    @Query("SELECT m.district,p.cardLabel, sum(p.amount) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.cardLabel,m.district")
    List<Object[]> revenueTransactionCoreGroupByCardLabelAndDistrict(Date fromDate, Date toDate);

    @Query("SELECT m.district,p.paymentMode, COUNT(p) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.paymentMode,m.district")
    List<Object[]> countTransactionCoreGroupByPaymentModeAndDistrict(Date fromDate, Date toDate);

    @Query("SELECT m.district,p.paymentMode, sum(p.amount) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.paymentMode,m.district")
    List<Object[]> revenueTransactionCoreGroupByPaymentModeAndDistrict(Date fromDate, Date toDate);

    @Query("SELECT m.district,p.tranType, COUNT(p) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.tranType,m.district")
    List<Object[]> countTransactionCoreGroupByTranTypeAndDistrict(Date fromDate, Date toDate);

    @Query("SELECT m.district,p.tranType, sum(p.amount) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.tranType,m.district")
    List<Object[]> revenueTransactionCoreGroupByTranTypeAndDistrict(Date fromDate, Date toDate);


}
