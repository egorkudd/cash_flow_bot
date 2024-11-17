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
                .userId(transaction.getUserId())
                .eTransaction(transaction.getType())
                .amount(transaction.getAmount())
                .eCurrency(transaction.getECurrency())
                .build();
    }

}
