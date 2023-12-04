package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "transaction_type"
)
@Data
@NoArgsConstructor
public class TransactionType implements java.io.Serializable {

    @Id
    @Column(name = "tran_type", unique = true, nullable = false, length = 10)
    private String tranType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", nullable = false)
    private Status status;
    @Column(name = "description", length = 100)
    private String description;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transactionType")
    private Set<TransactionSwitch> transactionSwitches = new HashSet<TransactionSwitch>(0);

}


