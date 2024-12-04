package com.ked.web.dto;

import com.ked.core.enums.ETransaction;
import com.ked.tg.utils.DateUtil;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class RegularTransactionDto {
    private Long userId;
    private String type;
    private String categoryName;
    private String title;
    private BigDecimal amount;
    private Date date;

    @Override
    public String toString() {
        return """
                ❗ Напоминание ❗
                Дата: %s
                Тип: %s
                Категория: %s
                Название: %s
                Сумма: %s
                """.formatted(DateUtil.convertDate(date), ETransaction.valueOf(type).getValue(), categoryName, title, amount);
    }
}