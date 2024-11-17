package com.ked.core.entities;

import com.ked.core.enums.ECurrency;
import com.ked.core.enums.ETransaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private ETransaction type;

    @JoinColumn(name = "category_id")
    private Long categoryId;

    private String title;

    private BigDecimal amount;

    private ECurrency eCurrency;

    @Column(name = "created_at")
    private Instant createdAt;
}
