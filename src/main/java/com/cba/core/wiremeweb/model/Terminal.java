package com.cba.core.wiremeweb.model;
// Generated Jun 20, 2023 2:49:48 PM by Hibernate Tools 4.3.1


import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * Terminal generated by hbm2java
 */
@Entity
@Table(name = "terminal"
)
public class Terminal implements java.io.Serializable {


    private Integer id;
    private Merchant merchant;
    private Status status;
    private User userByModifiedBy;
    private User userByCreatedBy;
    private String terminalId;
    private Integer deviceId;
    private Date createdAt;
    private Date updatedAt;
    private Set<SettlementInfo> settlementInfos = new HashSet<SettlementInfo>(0);

    public Terminal() {
    }


    public Terminal(Merchant merchant, Status status, User userByCreatedBy, String terminalId, Date createdAt, Date updatedAt) {
        this.merchant = merchant;
        this.status = status;
        this.userByCreatedBy = userByCreatedBy;
        this.terminalId = terminalId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Terminal(Merchant merchant, Status status, User userByModifiedBy, User userByCreatedBy, String terminalId, Integer deviceId, Date createdAt, Date updatedAt, Set<SettlementInfo> settlementInfos) {
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
    @JoinColumn(name = "merchantid", nullable = false)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    public User getUserByModifiedBy() {
        return this.userByModifiedBy;
    }

    public void setUserByModifiedBy(User userByModifiedBy) {
        this.userByModifiedBy = userByModifiedBy;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    public User getUserByCreatedBy() {
        return this.userByCreatedBy;
    }

    public void setUserByCreatedBy(User userByCreatedBy) {
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


