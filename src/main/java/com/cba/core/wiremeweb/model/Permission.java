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

@Entity
@Table(name = "permission"
)
@EntityListeners(AuditingEntityListener.class) // enable entity level auditing for create,modified attributes
@Data
@NoArgsConstructor
public class Permission implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private Resource resource;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
    @Column(name = "created_by", nullable = false, length = 45)
    @CreatedBy
    private String createdBy;
    @Column(name = "modified_by", length = 45)
    @LastModifiedBy
    private String modifiedBy;
    @Column(name = "created")
    private Byte created;
    @Column(name = "readd")
    private Byte readd;
    @Column(name = "updated")
    private Byte updated;
    @Column(name = "deleted")
    private Byte deleted;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, length = 19)
    @CreatedDate
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false, length = 19)
    @LastModifiedDate
    private Date updatedAt;

}


