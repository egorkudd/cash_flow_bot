package com.ked.core.dto;

import com.ked.core.enums.ECurrency;
import com.ked.core.enums.ETransaction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class TransactionDto {
    private Long userId;
    private ETransaction eTransaction;
    private BigDecimal amount;
    private ECurrency eCurrency;
}
