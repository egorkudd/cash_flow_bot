package com.ked.core.mappers;

import com.ked.core.dto.TransactionDto;
import com.ked.core.entities.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDto toDto(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        return TransactionDto.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .categoryId(transaction.getCategoryId())
                .title(transaction.getTitle())
                .eTransaction(transaction.getType())
                .amount(transaction.getAmount())
                .eCurrency(transaction.getECurrency())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

}
