package com.cba.core.wiremeweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "card_type"
)
@Data
@NoArgsConstructor
public class CardType implements java.io.Serializable {

    @Id
    @Column(name = "card_label", unique = true, nullable = false, length = 100)
    private String cardLabel;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", nullable = false)
    private Status status;
    @Column(name = "description", length = 100)
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, length = 19)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false, length = 19)
    private Date updatedAt;

}


