package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "push_device"
)
@Data
@NoArgsConstructor
public class PushDevice implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;
    @Column(name = "push_id", nullable = false, length = 100)
    private String pushId;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pushDevice")
    private Set<PushdeviceOperation> pushdeviceOperations = new HashSet<PushdeviceOperation>(0);

}


