package com.cba.core.wiremeweb.model;
// Generated Jun 20, 2023 2:49:48 PM by Hibernate Tools 4.3.1


import jakarta.persistence.*;


/**
 * StatusCategory generated by hbm2java
 */
@Entity
@Table(name = "status_category"
)
public class StatusCategory implements java.io.Serializable {


    private String categoryCode;
    private String description;

    public StatusCategory() {
    }


    public StatusCategory(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public StatusCategory(String categoryCode, String description) {
        this.categoryCode = categoryCode;
        this.description = description;
    }

    @Id
    @Column(name = "category_code", unique = true, nullable = false, length = 10)
    public String getCategoryCode() {
        return this.categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }


    @Column(name = "description", length = 100)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}


