package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "token_blacklist",
        uniqueConstraints = @UniqueConstraint(columnNames = "token")
)
@Data
@NoArgsConstructor
public class TokenBlacklist implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "token", unique = true, nullable = false, length = 1500)
    private String token;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiration", nullable = false, length = 19)
    private Instant expiration;

}
