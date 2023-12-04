package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "province")
@Data
@NoArgsConstructor
public class Province implements java.io.Serializable {

    @Id
    @Column(name = "code", unique = true, nullable = false, length = 20)
    private String code;
    @Column(name = "description", length = 100)
    private String description;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "province")
    private Set<District> districts = new HashSet<District>(0);

    public Province(String code) {
        this.code = code;
    }

}


