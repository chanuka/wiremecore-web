package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "terminal", uniqueConstraints = {@UniqueConstraint(name = "terminal_un", columnNames = {"terminal_id","merchant_id"})}
)
@EntityListeners(AuditingEntityListener.class) // enable entity level auditing for create,modified attributes
@Data
@NoArgsConstructor
public class Terminal implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", referencedColumnName = "merchant_id", nullable = false)
    private Merchant merchant;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", nullable = false)
    private Status status;
    @Column(name = "modified_by", length = 45)
    @LastModifiedBy
    private String userByModifiedBy;
    @Column(name = "created_by", nullable = false, length = 45)
    @CreatedBy
    private String userByCreatedBy;
    @Column(name = "terminal_id", nullable = false, length = 9, unique = true)
    private String terminalId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, length = 19)
    @CreatedDate
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false, length = 19)
    @LastModifiedDate
    private Date updatedAt;
    @Column(name = "is_void_enabled")
    private Byte isVoidEnabled;
    @Column(name = "is_offline_enabled")
    private Byte isOfflineEnabled;
    @Column(name = "is_preauth_enabled")
    private Byte isPreauthEnabled;
    @Column(name = "is_mke_enabled")
    private Byte isMkeEnabled;
    @Column(name = "daily_exp_sale")
    private Integer dailyExpSale = 0;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_settled", length = 19)
    private Date lastSettled;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency", nullable = false)
    private Currency currency;
    @Column(name = "remarks", length = 65535, columnDefinition = "TEXT")
    private String remarks;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "terminal")
    private Set<SettlementInfo> settlementInfos = new HashSet<SettlementInfo>(0);

}


