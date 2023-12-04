package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "resource"
)
@Data
@NoArgsConstructor
public class Resource implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "name", length = 20)
    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, length = 19)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false, length = 19)
    private Date updatedAt;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "resource")
    private Set<Permission> permissions = new HashSet<Permission>(0);

    public Resource(Integer resourceId) {
        this.id = resourceId;
    }

}


