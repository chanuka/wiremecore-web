package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "currency"
)
@Data
@NoArgsConstructor
public class Currency implements java.io.Serializable {

    @Id
    @Column(name = "code", unique = true, nullable = false, length = 6)
    private String code;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", nullable = false)
    private Status status;
    @Column(name = "description", length = 100)
    private String description;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "currency")
    private Set<Terminal> terminals = new HashSet<Terminal>(0);

    public Currency(String code) {
        this.code = code;
    }
}


