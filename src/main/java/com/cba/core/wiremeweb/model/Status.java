package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "status"
)
@Data
@NoArgsConstructor
public class Status implements java.io.Serializable {

    @Id
    @Column(name = "status_code", unique = true, nullable = false, length = 10)
    private String statusCode;
    @Column(name = "status_description", length = 100)
    private String statusDescription;
    @Column(name = "status_category", length = 10)
    private String statusCategory;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<Bank> banks = new HashSet<Bank>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<DeviceConfig> deviceConfigs = new HashSet<DeviceConfig>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<TransactionType> transactionTypes = new HashSet<TransactionType>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<Merchant> merchants = new HashSet<Merchant>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<MerchantCustomer> merchantCustomers = new HashSet<>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<SettlementInfo> settlementInfos = new HashSet<SettlementInfo>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<Terminal> terminals = new HashSet<Terminal>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<TransactionSwitch> transactionSwitches = new HashSet<TransactionSwitch>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<CardType> cardTypes = new HashSet<CardType>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<Device> devices = new HashSet<Device>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<User> users = new HashSet<User>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<Role> roles = new HashSet<Role>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<UserRole> userRoles = new HashSet<UserRole>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<UserType> userTypes = new HashSet<UserType>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<UserConfig> userConfigs = new HashSet<UserConfig>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<DeviceVendor> deviceVendors = new HashSet<DeviceVendor>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<DeviceModel> deviceModels = new HashSet<DeviceModel>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<Currency> currencies = new HashSet<>(0);

    public Status(String statusCode) {
        this.statusCode = statusCode;
    }

}


