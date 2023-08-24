package com.cba.core.wiremeweb.model;
// Generated Jun 20, 2023 2:49:48 PM by Hibernate Tools 4.3.1


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


/**
 * Status generated by hbm2java
 */
@Entity
@Table(name = "status"
)
public class Status implements java.io.Serializable {


    private String statusCode;
    private String statusDescription;
    private String statusCategory;
    private Set<Bank> banks = new HashSet<Bank>(0);
    private Set<DeviceConfig> deviceConfigs = new HashSet<DeviceConfig>(0);
    private Set<TransactionType> transactionTypes = new HashSet<TransactionType>(0);
    private Set<Merchant> merchants = new HashSet<Merchant>(0);
    private Set<MerchantCustomer> merchantCustomers = new HashSet<>(0);
    private Set<SettlementInfo> settlementInfos = new HashSet<SettlementInfo>(0);
    private Set<Terminal> terminals = new HashSet<Terminal>(0);
    private Set<TransactionSwitch> transactionSwitches = new HashSet<TransactionSwitch>(0);
    private Set<CardType> cardTypes = new HashSet<CardType>(0);
    private Set<Device> devices = new HashSet<Device>(0);
    private Set<User> users = new HashSet<User>(0);
    private Set<Role> roles = new HashSet<Role>(0);
    private Set<UserRole> userRoles = new HashSet<UserRole>(0);
    private Set<UserType> userTypes = new HashSet<UserType>(0);

    public Status() {
    }


    public Status(String statusCode) {
        this.statusCode = statusCode;
    }

    public Status(String statusCode, String statusDescription, String statusCategory, Set<Bank> banks, Set<DeviceConfig> deviceConfigs, Set<TransactionType> transactionTypes, Set<Merchant> merchants, Set<MerchantCustomer> merchantCustomers, Set<SettlementInfo> settlementInfos, Set<Terminal> terminals, Set<TransactionSwitch> transactionSwitches, Set<CardType> cardTypes, Set<Device> devices, Set<User> users, Set<Role> roles, Set<UserRole> userRoles, Set<UserType> userTypes) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
        this.statusCategory = statusCategory;
        this.banks = banks;
        this.deviceConfigs = deviceConfigs;
        this.transactionTypes = transactionTypes;
        this.merchants = merchants;
        this.merchantCustomers = merchantCustomers;
        this.settlementInfos = settlementInfos;
        this.terminals = terminals;
        this.transactionSwitches = transactionSwitches;
        this.cardTypes = cardTypes;
        this.devices = devices;
        this.users = users;
        this.roles = roles;
        this.userRoles = userRoles;
        this.userTypes = userTypes;
    }

    @Id
    @Column(name = "status_code", unique = true, nullable = false, length = 10)
    public String getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }


    @Column(name = "status_description", length = 100)
    public String getStatusDescription() {
        return this.statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }


    @Column(name = "status_category", length = 10)
    public String getStatusCategory() {
        return this.statusCategory;
    }

    public void setStatusCategory(String statusCategory) {
        this.statusCategory = statusCategory;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    public Set<Bank> getBanks() {
        return this.banks;
    }

    public void setBanks(Set<Bank> banks) {
        this.banks = banks;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    public Set<DeviceConfig> getDeviceConfigs() {
        return this.deviceConfigs;
    }

    public void setDeviceConfigs(Set<DeviceConfig> deviceConfigs) {
        this.deviceConfigs = deviceConfigs;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    public Set<TransactionType> getTransactionTypes() {
        return this.transactionTypes;
    }

    public void setTransactionTypes(Set<TransactionType> transactionTypes) {
        this.transactionTypes = transactionTypes;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    public Set<Merchant> getMerchants() {
        return this.merchants;
    }

    public void setMerchants(Set<Merchant> merchants) {
        this.merchants = merchants;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    public Set<MerchantCustomer> getMerchantCustomers() {
        return this.merchantCustomers;
    }

    public void setMerchantCustomers(Set<MerchantCustomer> merchantCustomers) {
        this.merchantCustomers = merchantCustomers;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    public Set<SettlementInfo> getSettlementInfos() {
        return this.settlementInfos;
    }

    public void setSettlementInfos(Set<SettlementInfo> settlementInfos) {
        this.settlementInfos = settlementInfos;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    public Set<Terminal> getTerminals() {
        return this.terminals;
    }

    public void setTerminals(Set<Terminal> terminals) {
        this.terminals = terminals;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    public Set<TransactionSwitch> getTransactionSwitches() {
        return this.transactionSwitches;
    }

    public void setTransactionSwitches(Set<TransactionSwitch> transactionSwitches) {
        this.transactionSwitches = transactionSwitches;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    public Set<CardType> getCardTypes() {
        return this.cardTypes;
    }

    public void setCardTypes(Set<CardType> cardTypes) {
        this.cardTypes = cardTypes;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    public Set<Device> getDevices() {
        return this.devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    public Set<UserRole> getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.roles = roles;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    public Set<UserType> getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(Set<UserType> userTypes) {
        this.userTypes = userTypes;
    }
}


