package com.ked.core.mappers;

import com.ked.core.dto.TransactionDto;
import com.ked.core.entities.Transaction;
import com.ked.core.repositories.CategoryRepository;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final CategoryRepository categoryRepository;

    public TransactionDto toDto(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        return TransactionDto.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .categoryId(transaction.getCategoryId())
                .categoryName(categoryRepository.findById(transaction.getCategoryId()).orElseThrow(() ->
                        new EntityNotFoundBotException(
                                "Категория не найдена по Id: " + transaction.getCategoryId()
                        )).getName()
                )
                .title(transaction.getTitle())
                .eTransaction(transaction.getType())
                .amount(transaction.getAmount())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

}
