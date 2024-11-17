package com.ked.core.services.impl;

import com.ked.core.entities.Category;
import com.ked.core.entities.Transaction;
import com.ked.core.enums.ETransaction;
import com.ked.core.repositories.CategoryRepository;
import com.ked.core.repositories.TransactionRepository;
import com.ked.core.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public List<Category> findAllByUserIdToChoose(Long userId) {
        Optional<Transaction> transactionOpt = transactionRepository.findByUserIdAndCreatedAtIsNull(userId);
        if (transactionOpt.isPresent()) {
            ETransaction type = transactionOpt.get().getType();
            if (type != null) {
                return categoryRepository.findAllByUserIdAndType(userId, type);
            }
        }

        return categoryRepository.findAll();
    }

    @Override
    public boolean exists(Long categoryId) {
        return categoryRepository.existsById(categoryId);
    }
}
