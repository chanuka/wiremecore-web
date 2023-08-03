package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_type"
)
public class UserType implements java.io.Serializable {
    private Integer id;
    private Status status;
    private String typeName;
    private Set<User> users = new HashSet<User>(0);

    public UserType() {
    }

    public UserType(Status status, String typeName, Set<User> users) {
        this.status = status;
        this.typeName = typeName;
        this.users = users;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", nullable = false)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Column(name = "type_name", length = 45)
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userType")
    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
