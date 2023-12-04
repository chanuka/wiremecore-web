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
@Table(name = "user"
)
@EntityListeners(AuditingEntityListener.class) // enable entity level auditing for create,modified attributes
@Data
@NoArgsConstructor
public class User implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_customer_id")
    private MerchantCustomer merchantCustomer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", nullable = false)
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_type", nullable = false)
    private UserType userType;
    @JoinColumn(name = "name", nullable = false)
    private String name;
    @Column(name = "user_name", nullable = false, length = 45)
    private String userName;
    @Column(name = "password", length = 65535, columnDefinition = "TEXT")
    private String password;
    @Column(name = "email", length = 50)
    private String email;
    @Column(name = "first_login", nullable = false)
    private Integer firstLogin = 0;
    @Column(name = "contact_no", length = 12)
    private String contactNo;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login_time", length = 19)
    private Date lastLoginTime;
    @Column(name = "login_attempt", nullable = false)
    private int loginAttempt;
    @Column(name = "session_id")
    private String sessionId;
    @Column(name = "created_by", nullable = false, length = 45)
    @CreatedBy
    private String createdBy;
    @Column(name = "modified_by", length = 45)
    @LastModifiedBy
    private String modifiedBy;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, length = 19)
    @CreatedDate
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", length = 19)
    @LastModifiedDate
    private Date updatedAt;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<TokenRefresh> refreshTokens = new HashSet<TokenRefresh>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userByUserId")
    private Set<UserRole> userRolesForUserId = new HashSet<UserRole>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<OnetimePassword> onetimePasswords = new HashSet<OnetimePassword>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<UserConfig> userConfigs = new HashSet<UserConfig>(0);

    public User(Integer userId) {
        this.id = userId;
    }
}


