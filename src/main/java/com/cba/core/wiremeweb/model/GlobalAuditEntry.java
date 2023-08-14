package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "global_audit_entry"
)
@EntityListeners(AuditingEntityListener.class)
public class GlobalAuditEntry {

    private int id;
    private String resource;
    @CreatedBy
    private String user;
    private String operation;
    private Integer effectedId;
    private String oldValue;
    private String newValue;
    @CreatedDate
    private Date createdAt;
    private String ipAddress;

    public GlobalAuditEntry() {
    }

    public GlobalAuditEntry(String resource, String operation, Integer effectedId, String oldValue, String newValue, String ipAddress) {
        this.resource = resource;
        this.operation = operation;
        this.effectedId = effectedId;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.ipAddress = ipAddress;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Integer getEffectedId() {
        return effectedId;
    }

    public void setEffectedId(Integer effectedId) {
        this.effectedId = effectedId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Column(name = "old_value", length = 65535, columnDefinition = "TEXT")
    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    @Column(name = "new_value", length = 65535, columnDefinition = "TEXT")
    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
