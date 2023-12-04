package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "token_refresh"
        , uniqueConstraints = @UniqueConstraint(columnNames = "token")
)
@Data
@NoArgsConstructor
public class TokenRefresh implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date", nullable = false, length = 26)
    private Instant expiryDate;
    @Column(name = "token", unique = true, nullable = false)
    private String token;

}


