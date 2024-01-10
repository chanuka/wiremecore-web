package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "transaction_core"
)
@EntityListeners(AuditingEntityListener.class) // enable entity level auditing for create,modified attributes
@Data
@NoArgsConstructor
public class TransactionCore implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "origin_id", nullable = false, length = 65)
    private String originId;
    @Column(name = "payment_mode", nullable = false, length = 10)
    private String paymentMode;
    @Column(name = "cust_mobile", length = 12)
    private String custMobile;
    @Column(name = "tran_type", length = 10)
    private String tranType;
    @Column(name = "card_label", length = 20)
    private String cardLabel;
    @Column(name = "terminal_id", length = 9)
    private String terminalId;
    @Column(name = "trace_no")
    private Integer traceNo;
    @Column(name = "invoice_no")
    private Integer invoiceNo;
    @Column(name = "amount", nullable = false)
    private Integer amount;
    @Column(name = "currency", nullable = false, length = 6)
    private String currency;
    @Column(name = "batch_no")
    private Integer batchNo;
    @Column(name = "pan", length = 25)
    private String pan;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time", nullable = false, length = 19)
    private Date dateTime;
    @Column(name = "exp_date", length = 5)
    private String expDate;
    @Column(name = "nii", length = 5)
    private String nii;
    @Column(name = "rrn", length = 20)
    private String rrn;
    @Column(name = "auth_code", length = 20)
    private String authCode;
    @Column(name = "sign_data", length = 10000)
    private String signData;
    @Column(name = "tip_amount")
    private Integer tipAmount;
    @Column(name = "entry_mode")
    private String entryMode;
    @Column(name = "dcc_currency")
    private String dccCurrency;
    @Column(name = "dcc_tran_amount")
    private Integer dccTranAmount;
    @Column(name = "issettled")
    private Boolean issettled;
    @Column(name = "settled_method")
    private Integer settledMethod;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, length = 19)
    @CreatedDate
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false, length = 19)
    @LastModifiedDate
    private Date updatedAt;
    @Column(name = "merchant_id", length = 16)
    private String merchantId;
    @Column(name = "lat")
    private Float lat;
    @Column(name = "lon")
    private Float lon;
    @Column(name = "is_away", nullable = false)
    private Boolean isAway = false;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transactionCore")
    private Set<SettlementInfo> settlementInfos = new HashSet<SettlementInfo>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transactionCore")
    private Set<EReceipt> eReceipts = new HashSet<EReceipt>(0);

}


