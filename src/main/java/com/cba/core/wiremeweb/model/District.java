package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "district")
@Data
@NoArgsConstructor
public class District implements java.io.Serializable {

    @Id
    @Column(name = "code", unique = true, nullable = false, length = 20)
    private String code;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province", nullable = false)
    private Province province;
    @Column(name = "description", length = 100)
    private String description;
    @Column(name = "lat", precision = 12, scale = 0)
    private Float lat;
    @Column(name = "lon", precision = 12, scale = 0)
    private Float lon;

}


