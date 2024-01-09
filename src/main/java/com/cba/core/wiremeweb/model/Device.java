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
@Table(name = "device"
)
@EntityListeners(AuditingEntityListener.class) // enable entity level auditing for create,modified attributes
@Data
@NoArgsConstructor
public class Device implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", nullable = false)
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_Id", nullable = false)
    private DeviceModel deviceModel;
    @Column(name = "created_by", nullable = false, length = 45)
    @CreatedBy
    private String userByCreatedBy;
    @Column(name = "modified_by", length = 45)
    @LastModifiedBy
    private String userByModifiedBy;
    @Column(name = "serial_no", nullable = false, length = 150)
    private String serialNo;
    @Column(name = "emi_no", nullable = false, length = 50)
    private String emiNo;
    @Column(name = "device_type", nullable = false, length = 20)
    private String deviceType;
    @Column(name = "unique_id")
    private String uniqueId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_active", length = 19)
    private Date lastActive;
    @Column(name = "lat")
    private Float lat;
    @Column(name = "lon")
    private Float lon;
    @Column(name = "is_away", nullable = false)
    private Boolean isAway = false;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, length = 19)
    @CreatedDate
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", length = 19)
    @LastModifiedDate
    private Date updatedAt;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
    private Set<PushDevice> pushDevices = new HashSet<PushDevice>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
    private Set<DeviceConfig> deviceConfigs = new HashSet<DeviceConfig>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
    private Set<User> users = new HashSet<User>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
    private Set<Terminal> terminals = new HashSet<Terminal>(0);

    public Device(Integer deviceId) {
        this.id = deviceId;
    }

}


