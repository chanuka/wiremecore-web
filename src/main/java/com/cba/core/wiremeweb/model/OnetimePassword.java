package com.cba.core.wiremeweb.model;
// Generated Jun 20, 2023 2:49:48 PM by Hibernate Tools 4.3.1


import jakarta.persistence.*;

import java.util.Date;


/**
 * OnetimePassword generated by hbm2java
 */
@Entity
@Table(name = "onetime_password"
)
public class OnetimePassword implements java.io.Serializable {

    private Integer id;
    private User user;
    private int value;
    private Date expireson;

    public OnetimePassword() {
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
    @JoinColumn(name = "user_id")
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "value", nullable = false)
    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expireson", length = 19)
    public Date getExpireson() {
        return this.expireson;
    }

    public void setExpireson(Date expireson) {
        this.expireson = expireson;
    }

}


