package com.ked.core.dto;

import com.ked.core.enums.ECurrency;
import com.ked.core.enums.ETransaction;
import com.ked.tg.utils.DateUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TransactionDto {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String categoryName;
    private String title;
    private ETransaction eTransaction;
    private BigDecimal amount;
    private ECurrency eCurrency;
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        String date = DateUtil.convertDate(createdAt);
        return title == null
                ? "[%s] {%s} (%s) %s".formatted(date, eTransaction.getValue(), categoryName, amount)
                : "[%s] {%s} (%s) %s %s".formatted(date, eTransaction.getValue(), categoryName, title, amount);

    }
}
