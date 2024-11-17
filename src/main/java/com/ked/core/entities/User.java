package com.ked.core.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Setter
@Getter
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "tg_id")
    private Long tgId;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "created_at")
    private Instant createdAt;

    public User(Long tgId, String name) {
        this.tgId = tgId;
        this.name = name;
        this.createdAt = Instant.now();
    }
}
