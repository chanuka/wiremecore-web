package com.cba.core.wiremeweb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "status_category"
)
@Data
@NoArgsConstructor
public class StatusCategory implements java.io.Serializable {

    @Id
    @Column(name = "category_code", unique = true, nullable = false, length = 10)
    private String categoryCode;
    @Column(name = "description", length = 100)
    private String description;

}


