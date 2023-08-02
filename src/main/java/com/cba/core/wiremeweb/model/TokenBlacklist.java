package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "token_blacklist",
        uniqueConstraints = @UniqueConstraint(columnNames = "token")
)
public class TokenBlacklist implements java.io.Serializable {

    private Long id;
    private String token;
    private Instant expiration;

    public TokenBlacklist() {
    }

    public TokenBlacklist(Long id, String token, Instant expiration) {
        this.id = id;
        this.token = token;
        this.expiration = expiration;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "token", unique = true, nullable = false, length = 1500)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiration", nullable = false, length = 19)
    public Instant getExpiration() {
        return expiration;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }
}
