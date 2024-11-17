package com.ked.core.dto;

import com.ked.core.enums.ECurrency;
import com.ked.core.enums.ETransaction;
import com.ked.tg.utils.DateUtil;
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


    @Override
    public String toString() {
        String date = DateUtil.convertDate(createdAt);

        return title == null
                ? "[%s] {%s} (%d) %s".formatted(date, eTransaction.getValue(), categoryId, amount)
                : "[%s] {%s} (%d) %s %s".formatted(date, eTransaction.getValue(), categoryId, title, amount);

    }
}
