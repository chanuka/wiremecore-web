package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "pushdevice_operation"
)
@Data
@NoArgsConstructor
public class PushdeviceOperation implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation", nullable = false)
    private Operation operation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "push_device", nullable = false)
    private PushDevice pushDevice;
    @Column(name = "status", nullable = false, length = 10)
    private String status;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, length = 19)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at", nullable = false, length = 19)
    private Date modifiedAt;
    @Column(name = "push_data", length = 100)
    private String pushData;

}


