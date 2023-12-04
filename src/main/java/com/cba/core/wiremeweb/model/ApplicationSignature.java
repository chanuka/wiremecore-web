package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "application_signature"
)
@Data
@NoArgsConstructor
public class ApplicationSignature implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "app_version", nullable = false, length = 10)
    private String appVersion;
    @Column(name = "app_signature", nullable = false, length = 65535, columnDefinition = "TEXT")
    private String appSignature;
    @Column(name = "packagefile_signature")
    private String packagefileSignature;
    @Column(name = "sysfile_size")
    private Integer sysfileSize;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", length = 19)
    private Date createdAt;

}


