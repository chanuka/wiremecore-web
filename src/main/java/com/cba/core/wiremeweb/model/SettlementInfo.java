package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Table(name = "settlement_info"
)
@Data
@NoArgsConstructor
public class SettlementInfo implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", nullable = false)
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terminal_id", nullable = false)
    private Terminal terminal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tran_id", nullable = false)
    private TransactionCore transactionCore;
    @Column(name = "batch_id", nullable = false)
    private long batchId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time", nullable = false, length = 19)
    private Date dateTime;

}


