package com.ked.core.dto;

import com.ked.core.enums.ECurrency;
import com.ked.core.enums.ETransaction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
public class TransactionDto {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String title;
    private ETransaction eTransaction;
    private BigDecimal amount;
    private ECurrency eCurrency;
    private Instant createdAt;
}
