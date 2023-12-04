package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "global_audit_entry"
)
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class GlobalAuditEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Column(name = "resource")
    private String resource;
    @Column(name = "user")
    @CreatedBy
    private String user;
    @Column(name = "operation")
    private String operation;
    @Column(name = "effected_id")
    private Integer effectedId;
    @Column(name = "old_value", length = 65535, columnDefinition = "TEXT")
    private String oldValue;
    @Column(name = "new_value", length = 65535, columnDefinition = "TEXT")
    private String newValue;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", length = 26)
    @CreatedDate
    private Date createdAt;
    @Column(name = "ip_address")
    private String ipAddress;

    public GlobalAuditEntry(String resource, String operation, Integer effectedId, String oldValue, String newValue, String ipAddress) {
        this.resource = resource;
        this.operation = operation;
        this.effectedId = effectedId;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.ipAddress = ipAddress;
    }
}
