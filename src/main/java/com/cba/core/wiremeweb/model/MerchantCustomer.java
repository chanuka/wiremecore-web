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
 * MerchantCustomer generated by hbm2java
 */
@Entity
@Table(name = "merchant_customer"
)
@EntityListeners(AuditingEntityListener.class) // enable entity level auditing for create,modified attributes
public class MerchantCustomer implements java.io.Serializable {


    private int id;
    private Status status;
    private String name;
    private String address;
    private String email;
    private String contactNo;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String modifiedBy;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    private Set<User> users = new HashSet<User>(0);
    private Set<Merchant> merchants = new HashSet<Merchant>(0);

    public MerchantCustomer() {
    }

    public MerchantCustomer(Integer id) {
        this.id = id;
    }

    public MerchantCustomer(int id, String name, String address, String email, String contactNo, String createdBy, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.contactNo = contactNo;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public MerchantCustomer(int id, Status status, String name, String address, String email, String contactNo, String createdBy, String modifiedBy, Date createdAt, Date updatedAt, Set<User> users, Set<Merchant> merchants) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.address = address;
        this.email = email;
        this.contactNo = contactNo;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.users = users;
        this.merchants = merchants;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Column(name = "address", nullable = false, length = 150)
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Column(name = "email", nullable = false, length = 50)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Column(name = "contact_no", nullable = false, length = 15)
    public String getContactNo() {
        return this.contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }


    @Column(name = "created_by", nullable = false, length = 45)
    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


    @Column(name = "modified_by", length = 45)
    public String getModifiedBy() {
        return this.modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "merchantCustomer")
    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "merchantCustomer")
    public Set<Merchant> getMerchants() {
        return this.merchants;
    }

    public void setMerchants(Set<Merchant> merchants) {
        this.merchants = merchants;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status")
    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}


