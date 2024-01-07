package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "e_receipt"
)
@EntityListeners(AuditingEntityListener.class) // enable entity level auditing for create,modified attributes
@Data
@NoArgsConstructor
public class EReceipt implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private TransactionCore transactionCore;
    @Column(name = "email", length = 40)
    private String email;
    @Column(name = "contact_no", length = 15)
    private String contactNo;
    @Column(name = "receipt_type", length = 15)
    private String receiptType;
    @Column(name = "isSentMail")
    private Boolean is_sent_mail;
    @Column(name = "is_sent_sms")
    private Boolean isSentSms;
    @CreatedDate
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", length = 19)
    @LastModifiedDate
    private Date updatedAt;

    public EReceipt(TransactionCore transactionCore, String email, String contactNo, String receiptType, Boolean is_sent_mail, Boolean isSentSms) {
        this.transactionCore = transactionCore;
        this.email = email;
        this.contactNo = contactNo;
        this.receiptType = receiptType;
        this.is_sent_mail = is_sent_mail;
        this.isSentSms = isSentSms;
    }
}
