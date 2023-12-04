package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "transaction_switch"
)
@Data
@NoArgsConstructor
public class TransactionSwitch implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank")
    private Bank bank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status")
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tran_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "session_id", nullable = false, length = 100)
    private String sessionId;

    @Column(name = "mti", nullable = false, length = 4)
    private String mti;

    @Column(name = "pan", length = 512)
    private String pan;

    @Column(name = "processing_code", nullable = false, length = 6)
    private String processingCode;

    @Column(name = "tran_amount", length = 12)
    private String tranAmount;

    @Column(name = "settle_amount", length = 12)
    private String settleAmount;

    @Column(name = "card_holder_bill_amount", length = 12)
    private String cardHolderBillAmount;

    @Column(name = "transmission_date", length = 10)
    private String transmissionDate;

    @Column(name = "card_holder_billing_fee_amount", length = 8)
    private String cardHolderBillingFeeAmount;

    @Column(name = "conversion_rate_settlement", length = 8)
    private String conversionRateSettlement;

    @Column(name = "conversion_rate_billing", length = 8)
    private String conversionRateBilling;

    @Column(name = "trace_no", nullable = false, length = 6)
    private String traceNo;

    @Column(name = "local_tran_time", length = 6)
    private String localTranTime;

    @Column(name = "local_tran_date", length = 4)
    private String localTranDate;
    @Column(name = "expire_date", length = 256)
    private String expireDate;

    @Column(name = "settlement_date", length = 4)
    private String settlementDate;

    @Column(name = "currency_conversion_date", length = 4)
    private String currencyConversionDate;

    @Column(name = "capture_date", length = 4)
    private String captureDate;

    @Column(name = "merchant_type", length = 4)
    private String merchantType;

    @Column(name = "acq_institute_country_code", length = 3)
    private String acqInstituteCountryCode;

    @Column(name = "pan_ext_country_code", length = 3)
    private String panExtCountryCode;

    @Column(name = "fwd_institiute_country_code", length = 3)
    private String fwdInstitiuteCountryCode;

    @Column(name = "entry_mode", length = 3)
    private String entryMode;
    @Column(name = "pan_seq_no", length = 3)
    private String panSeqNo;

    @Column(name = "nii", length = 3)
    private String nii;

    @Column(name = "condition_code", length = 2)
    private String conditionCode;

    @Column(name = "capture_code", length = 2)
    private String captureCode;

    @Column(name = "auth_id_length", length = 1)
    private String authIdLength;

    @Column(name = "tran_fee", length = 12)
    private String tranFee;

    @Column(name = "settle_fee", length = 12)
    private String settleFee;

    @Column(name = "tran_processing_fee", length = 12)
    private String tranProcessingFee;

    @Column(name = "settlement_processing_fee", length = 12)
    private String settlementProcessingFee;

    @Column(name = "acq_institute_id_code", length = 14)
    private String acqInstituteIdCode;

    @Column(name = "forward_institute_id_code", length = 14)
    private String forwardInstituteIdCode;

    @Column(name = "ext_pan", length = 30)
    private String extPan;

    @Column(name = "track_two_data", length = 256)
    private String trackTwoData;

    @Column(name = "track_three_data", length = 110)
    private String trackThreeData;

    @Column(name = "retreval_reference_no", length = 12)
    private String retrevalReferenceNo;

    @Column(name = "auth_code", length = 6)
    private String authCode;

    @Column(name = "response_code", length = 3)
    private String responseCode;

    @Column(name = "service_restriction_code", length = 3)
    private String serviceRestrictionCode;

    @Column(name = "terminal_id", nullable = false, length = 8)
    private String terminalId;
    @Column(name = "merchant_id", length = 15)
    private String merchantId;

    @Column(name = "card_acceptance_location", length = 40)
    private String cardAcceptanceLocation;

    @Column(name = "adnitional_response_data", length = 30)
    private String adnitionalResponseData;

    @Column(name = "field_46", length = 999)
    private String field46;

    @Column(name = "field_47", length = 999)
    private String field47;

    @Column(name = "field_48", length = 999)
    private String field48;

    @Column(name = "transaction_currency_code", length = 3)
    private String transactionCurrencyCode;

    @Column(name = "track_one_data", length = 100)
    private String trackOneData;

    @Column(name = "settlement_currency_code", length = 3)
    private String settlementCurrencyCode;

    @Column(name = "card_holder_billing_currency_code", length = 3)
    private String cardHolderBillingCurrencyCode;

    @Column(name = "sec_related_info", length = 16)
    private String secRelatedInfo;

    @Column(name = "addition_amount", length = 125)
    private String additionAmount;

    @Column(name = "emv_data", length = 1024)
    private String emvData;

    @Column(name = "field_56", length = 999)
    private String field56;

    @Column(name = "field_57", length = 999)
    private String field57;

    @Column(name = "field_58", length = 59)
    private String field58;

    @Column(name = "field_59", length = 999)
    private String field59;

    @Column(name = "field_60", length = 999)
    private String field60;

    @Column(name = "field_61", length = 999)
    private String field61;
    @Column(name = "field_62", length = 999)
    private String field62;

    @Column(name = "field_63", length = 999)
    private String field63;

    @Column(name = "mac_data", length = 32)
    private String macData;

    @Column(name = "settlement_status", nullable = false)
    private boolean settlementStatus;

    @Column(name = "card_label", length = 100)
    private String cardLabel;

    @Column(name = "serial_number", nullable = false, length = 100)
    private String serialNumber;

    @Column(name = "imei_number", nullable = false, length = 100)
    private String imeiNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, length = 19)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false, length = 19)
    private Date updatedAt;

}


