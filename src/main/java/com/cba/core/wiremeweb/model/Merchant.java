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
@Table(name = "merchant", uniqueConstraints = {@UniqueConstraint(name = "merchant_un", columnNames = {"merchant_id"})}
)
@EntityListeners(AuditingEntityListener.class) // enable entity level auditing for create,modified attributes
@Data
@NoArgsConstructor
public class Merchant implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank")
    private Bank bank;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_customer_id", nullable = false)
    private MerchantCustomer merchantCustomer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status")
    private Status status;
    @Column(name = "created_by", nullable = false, length = 45)
    @CreatedBy
    private String createdBy;
    @Column(name = "modified_by", length = 45)
    @LastModifiedBy
    private String modifiedBy;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "merchant_id", nullable = false, length = 16, unique = true)
    private String merchantId;
    @Column(name = "email", nullable = false, length = 40)
    private String email;
    @Column(name = "contact_no", length = 15)
    private String contactNo;
    @Column(name = "province", nullable = false, length = 30)
    private String province;
    @Column(name = "district", nullable = false, length = 30)
    private String district;
    @Column(name = "lat")
    private Float lat;
    @Column(name = "lon")
    private Float lon;
    @Column(name = "radius")
    private Integer radius;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, length = 19)
    @CreatedDate
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false, length = 19)
    @LastModifiedDate
    private Date updatedAt;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "merchant")
    private Set<Terminal> terminals = new HashSet<Terminal>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "merchant")
    private Set<User> users = new HashSet<User>(0);

}


