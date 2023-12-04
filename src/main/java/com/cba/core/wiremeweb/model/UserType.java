package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_type"
)
@Data
@NoArgsConstructor
public class UserType implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", nullable = false)
    private Status status;
    @Column(name = "type_name", length = 45)
    private String typeName;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userType")
    private Set<User> users = new HashSet<User>(0);

    public UserType(Integer id) {
        this.id = id;
    }

}
