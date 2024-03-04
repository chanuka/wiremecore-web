package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bank"
)
@Data
@NoArgsConstructor
public class Bank implements java.io.Serializable {

    @Id
    @Column(name = "bank_code", unique = true, nullable = false, length = 16)
    private String bankCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", nullable = false)
    private Status status;
    @Column(name = "bank_name", nullable = false, length = 100)
    private String bankName;
    @Column(name = "bank_urll", length = 100)
    private String bankUrll;
    @Column(name = "bank_nii", length = 100)
    private String bankNii;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bank")
    private Set<Merchant> merchants = new HashSet<Merchant>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bank")
    private Set<TransactionSwitch> transactionSwitches = new HashSet<TransactionSwitch>(0);

    public Bank(String bankCode) {
        this.bankCode = bankCode;
    }
}


