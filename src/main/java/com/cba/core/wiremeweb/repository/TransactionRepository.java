package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.TransactionCore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionCore, Integer>, JpaSpecificationExecutor<TransactionCore> {

//    @Query("SELECT p.cardLabel, COUNT(p) FROM TransactionCore p WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.cardLabel")
//    List<Object[]> countTransactionCoreGroupByCardLabel(Date fromDate, Date toDate);
//
//    @Query("SELECT p.cardLabel, sum(p.amount) FROM TransactionCore p WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.cardLabel")
//    List<Object[]> revenueTransactionCoreGroupByCardLabel(Date fromDate, Date toDate);
//
//    @Query("SELECT p.paymentMode, COUNT(p) FROM TransactionCore p WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.paymentMode")
//    List<Object[]> countTransactionCoreGroupByPaymentMode(Date fromDate, Date toDate);
//
//    @Query("SELECT p.paymentMode, sum(p.amount) FROM TransactionCore p WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.paymentMode")
//    List<Object[]> revenueTransactionCoreGroupByPaymentMode(Date fromDate, Date toDate);
//
//    @Query("SELECT p.tranType, COUNT(p) FROM TransactionCore p WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.tranType")
//    List<Object[]> countTransactionCoreGroupByTranType(Date fromDate, Date toDate);
//
//    @Query("SELECT p.tranType, sum(p.amount) FROM TransactionCore p WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.tranType")
//    List<Object[]> revenueTransactionCoreGroupByTranType(Date fromDate, Date toDate);

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

    @Query("SELECT m.province,p.cardLabel, COUNT(p) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.cardLabel,m.province")
    List<Object[]> countTransactionCoreGroupByCardLabelAndProvince(Date fromDate, Date toDate);

    @Query("SELECT m.province,p.cardLabel, sum(p.amount) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.cardLabel,m.province")
    List<Object[]> revenueTransactionCoreGroupByCardLabelAndProvince(Date fromDate, Date toDate);

    @Query("SELECT m.province,p.paymentMode, COUNT(p) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.paymentMode,m.province")
    List<Object[]> countTransactionCoreGroupByPaymentModeAndProvince(Date fromDate, Date toDate);

    @Query("SELECT m.province,p.paymentMode, sum(p.amount) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.paymentMode,m.province")
    List<Object[]> revenueTransactionCoreGroupByPaymentModeAndProvince(Date fromDate, Date toDate);

    @Query("SELECT m.province,p.tranType, COUNT(p) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.tranType,m.province")
    List<Object[]> countTransactionCoreGroupByTranTypeAndProvince(Date fromDate, Date toDate);

    @Query("SELECT m.province,p.tranType, sum(p.amount) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.tranType,m.province")
    List<Object[]> revenueTransactionCoreGroupByTranTypeAndProvince(Date fromDate, Date toDate);

    @Query("SELECT m.merchantId,p.cardLabel, COUNT(p) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.cardLabel,m.merchantId")
    List<Object[]> countTransactionCoreGroupByCardLabelAndMerchant(Date fromDate, Date toDate);

    @Query("SELECT m.merchantId,p.cardLabel, sum(p.amount) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.cardLabel,m.merchantId")
    List<Object[]> revenueTransactionCoreGroupByCardLabelAndMerchant(Date fromDate, Date toDate);

    @Query("SELECT m.merchantId,p.paymentMode, COUNT(p) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.paymentMode,m.merchantId")
    List<Object[]> countTransactionCoreGroupByPaymentModeAndMerchant(Date fromDate, Date toDate);

    @Query("SELECT m.merchantId,p.paymentMode, sum(p.amount) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.paymentMode,m.merchantId")
    List<Object[]> revenueTransactionCoreGroupByPaymentModeAndMerchant(Date fromDate, Date toDate);

    @Query("SELECT m.merchantId,p.tranType, COUNT(p) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.tranType,m.merchantId")
    List<Object[]> countTransactionCoreGroupByTranTypeAndMerchant(Date fromDate, Date toDate);

    @Query("SELECT m.merchantId,p.tranType, sum(p.amount) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.tranType,m.merchantId")
    List<Object[]> revenueTransactionCoreGroupByTranTypeAndMerchant(Date fromDate, Date toDate);

    @Query("SELECT m.merchantCustomer.name,p.cardLabel, COUNT(p) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.cardLabel,m.merchantCustomer.name")
    List<Object[]> countTransactionCoreGroupByCardLabelAndPartner(Date fromDate, Date toDate);

    @Query("SELECT m.merchantCustomer.name,p.cardLabel, sum(p.amount) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.cardLabel,m.merchantCustomer.name")
    List<Object[]> revenueTransactionCoreGroupByCardLabelAndPartner(Date fromDate, Date toDate);

    @Query("SELECT m.merchantCustomer.name,p.paymentMode, COUNT(p) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.paymentMode,m.merchantCustomer.name")
    List<Object[]> countTransactionCoreGroupByPaymentModeAndPartner(Date fromDate, Date toDate);

    @Query("SELECT m.merchantCustomer.name,p.paymentMode, sum(p.amount) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.paymentMode,m.merchantCustomer.name")
    List<Object[]> revenueTransactionCoreGroupByPaymentModeAndPartner(Date fromDate, Date toDate);

    @Query("SELECT m.merchantCustomer.name,p.tranType, COUNT(p) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.tranType,m.merchantCustomer.name")
    List<Object[]> countTransactionCoreGroupByTranTypeAndPartner(Date fromDate, Date toDate);

    @Query("SELECT m.merchantCustomer.name,p.tranType, sum(p.amount) FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
            "WHERE p.dateTime BETWEEN :fromDate AND :toDate GROUP BY p.tranType,m.merchantCustomer.name")
    List<Object[]> revenueTransactionCoreGroupByTranTypeAndPartner(Date fromDate, Date toDate);


}
