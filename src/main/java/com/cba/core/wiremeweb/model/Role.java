package com.cba.core.wiremeweb.model;
// Generated Jun 20, 2023 2:49:48 PM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;


/**
 * Role generated by hbm2java
 */
@Entity
@Table(name = "role"
)
public class Role implements java.io.Serializable {


    private Integer id;
    private Status status;
    private String roleName;
    private int createdBy;
    private Integer modifiedBy;
    private Date createdAt;
    private Date updatedAt;
    private Set<Permission> permissions = new HashSet<Permission>(0);
    private Set<UserRole> userRoles = new HashSet<UserRole>(0);

    public Role() {
    }


    public Role(Status status, int createdBy, Date createdAt, Date updatedAt) {
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Role(Status status, String roleName, int createdBy, Integer modifiedBy, Date createdAt, Date updatedAt, Set<Permission> permissions, Set<UserRole> userRoles) {
        this.status = status;
        this.roleName = roleName;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.permissions = permissions;
        this.userRoles = userRoles;
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
    @JoinColumn(name = "status", nullable = false)
    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    @Column(name = "role_name", length = 45)
    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


    @Column(name = "created_by", nullable = false)
    public int getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }


    @Column(name = "modified_by")
    public Integer getModifiedBy() {
        return this.modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    public Set<Permission> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    public Set<UserRole> getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }


}


