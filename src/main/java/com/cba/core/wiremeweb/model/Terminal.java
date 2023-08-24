package com.cba.core.wiremeweb.model;
// Generated Jun 20, 2023 2:49:48 PM by Hibernate Tools 4.3.1


import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * Terminal generated by hbm2java
 */
@Entity
@Table(name = "terminal"
)
@EntityListeners(AuditingEntityListener.class) // enable entity level auditing for create,modified attributes
public class Terminal implements java.io.Serializable {


    private Integer id;
    private Merchant merchant;
    private Status status;
    @LastModifiedBy
    private String userByModifiedBy;
    @CreatedBy
    private String userByCreatedBy;
    private String terminalId;
    private Integer deviceId;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    private Set<SettlementInfo> settlementInfos = new HashSet<SettlementInfo>(0);

    public Terminal() {
    }


    public Terminal(Merchant merchant, Status status, String userByCreatedBy, String terminalId, Date createdAt, Date updatedAt) {
        this.merchant = merchant;
        this.status = status;
        this.userByCreatedBy = userByCreatedBy;
        this.terminalId = terminalId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Terminal(Merchant merchant, Status status, String userByModifiedBy, String userByCreatedBy, String terminalId, Integer deviceId, Date createdAt, Date updatedAt, Set<SettlementInfo> settlementInfos) {
        this.merchant = merchant;
        this.status = status;
        this.userByModifiedBy = userByModifiedBy;
        this.userByCreatedBy = userByCreatedBy;
        this.terminalId = terminalId;
        this.deviceId = deviceId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.settlementInfos = settlementInfos;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    public Merchant getMerchant() {
        return this.merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", nullable = false)
    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Column(name = "modified_by", length = 45)
    public String getUserByModifiedBy() {
        return this.userByModifiedBy;
    }

    public void setUserByModifiedBy(String userByModifiedBy) {
        this.userByModifiedBy = userByModifiedBy;
    }

    @Column(name = "created_by", nullable = false, length = 45)
    public String getUserByCreatedBy() {
        return this.userByCreatedBy;
    }

    public void setUserByCreatedBy(String userByCreatedBy) {
        this.userByCreatedBy = userByCreatedBy;
    }


    @Column(name = "terminal_id", nullable = false, length = 9)
    public String getTerminalId() {
        return this.terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }


    @Column(name = "device_id")
    public Integer getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, length = 19)
    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false, length = 19)
    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "terminal")
    public Set<SettlementInfo> getSettlementInfos() {
        return this.settlementInfos;
    }

    public void setSettlementInfos(Set<SettlementInfo> settlementInfos) {
        this.settlementInfos = settlementInfos;
    }


}


