package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "onetime_password"
)
@Data
@NoArgsConstructor
public class OnetimePassword implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_name", referencedColumnName = "user_name")
    private User user;
    @Column(name = "value", length = 65535, columnDefinition = "TEXT", nullable = false)
    private String value;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expireson", length = 19)
    private Date expireson;

}


