package com.cba.core.wiremeweb.model;
// Generated Jun 20, 2023 2:49:48 PM by Hibernate Tools 4.3.1


import jakarta.persistence.*;

import java.util.Date;


/**
 * PushdeviceOperation generated by hbm2java
 */
@Entity
@Table(name = "pushdevice_operation"
)
public class PushdeviceOperation implements java.io.Serializable {


    private Long id;
    private Operation operation;
    private PushDevice pushDevice;
    private String status;
    private Date createdAt;
    private Date modifiedAt;
    private String pushData;

    public PushdeviceOperation() {
    }


    public PushdeviceOperation(Operation operation, PushDevice pushDevice, String status, Date createdAt, Date modifiedAt) {
        this.operation = operation;
        this.pushDevice = pushDevice;
        this.status = status;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public PushdeviceOperation(Operation operation, PushDevice pushDevice, String status, Date createdAt, Date modifiedAt, String pushData) {
        this.operation = operation;
        this.pushDevice = pushDevice;
        this.status = status;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.pushData = pushData;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation", nullable = false)
    public Operation getOperation() {
        return this.operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "push_device", nullable = false)
    public PushDevice getPushDevice() {
        return this.pushDevice;
    }

    public void setPushDevice(PushDevice pushDevice) {
        this.pushDevice = pushDevice;
    }


    @Column(name = "status", nullable = false, length = 10)
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
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
    @Column(name = "modified_at", nullable = false, length = 19)
    public Date getModifiedAt() {
        return this.modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }


    @Column(name = "push_data", length = 100)
    public String getPushData() {
        return this.pushData;
    }

    public void setPushData(String pushData) {
        this.pushData = pushData;
    }


}


