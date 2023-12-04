package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "operation"
)
@Data
@NoArgsConstructor
public class Operation implements java.io.Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private long id;
    @Column(name = "operation_code", nullable = false, length = 10)
    private String operationCode;
    @Column(name = "description", length = 100)
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, length = 19)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at", length = 19)
    private Date modifiedAt;
    @Column(name = "status", nullable = false, length = 10)
    private String status;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "operation")
    private Set<PushdeviceOperation> pushdeviceOperations = new HashSet<PushdeviceOperation>(0);

}


