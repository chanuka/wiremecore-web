package com.cba.core.wiremeweb.model;
// Generated Jun 20, 2023 2:49:48 PM by Hibernate Tools 4.3.1


import jakarta.persistence.*;

import java.util.Date;


/**
 * Permission generated by hbm2java
 */
@Entity
@Table(name = "permission"
)
public class Permission implements java.io.Serializable {

    private Integer id;
    private Resource resource;
    private Role role;
    private User userByModifiedBy;
    private User userByCreatedBy;
    private Byte created;
    private Byte readd;
    private Byte updated;
    private Byte deleted;
    private Date createdAt;
    private Date updatedAt;

    public Permission() {
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
    @JoinColumn(name = "resource_id")
    public Resource getResource() {
        return this.resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    @Column(name = "created")
    public Byte getCreated() {
        return this.created;
    }

    public void setCreated(Byte created) {
        this.created = created;
    }

    @Column(name = "readd")
    public Byte getReadd() {
        return this.readd;
    }

    public void setReadd(Byte readd) {
        this.readd = readd;
    }

    @Column(name = "updated")
    public Byte getUpdated() {
        return this.updated;
    }

    public void setUpdated(Byte updated) {
        this.updated = updated;
    }

    @Column(name = "deleted")
    public Byte getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
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

}


