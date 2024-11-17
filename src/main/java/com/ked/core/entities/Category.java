package com.ked.core.entities;

import com.ked.core.enums.ETransaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    private Long userId;

    private String name;

    @Enumerated(EnumType.STRING)
    private ETransaction type;

    @Column(name = "created_at")
    private Instant createdAt;
}