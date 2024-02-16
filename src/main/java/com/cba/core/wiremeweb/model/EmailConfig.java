package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "email_config", uniqueConstraints = {@UniqueConstraint(name = "email_config_un", columnNames = {"action"})}
)
@EntityListeners(AuditingEntityListener.class) // enable entity level auditing for create,modified attributes
@Data
@NoArgsConstructor
public class EmailConfig implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", length = 19)
    @CreatedDate
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", length = 19)
    @LastModifiedDate
    private Date updatedAt;
    @Column(name = "action", columnDefinition = "VARCHAR(30)", nullable = false, unique = true, updatable = false)
    private String action;
    @Column(name = "to_list", columnDefinition = "VARCHAR(500) default ''")
    private String to;
    @Column(name = "cc", columnDefinition = "VARCHAR(500) default ''")
    private String cc;
    @Column(name = "bcc", columnDefinition = "VARCHAR(500) default ''")
    private String bcc;
}
