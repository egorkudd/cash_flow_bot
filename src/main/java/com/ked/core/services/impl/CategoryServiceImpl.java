package com.ked.core.services.impl;

import com.ked.core.entities.Category;
import com.ked.core.entities.Transaction;
import com.ked.core.enums.ETransaction;
import com.ked.core.repositories.CategoryRepository;
import com.ked.core.repositories.TransactionRepository;
import com.ked.core.repositories.UserRepository;
import com.ked.core.services.CategoryService;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

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

    @Override
    public void setName(String name, Long userId) {
        checkUser(userId);

        Category category = getCollectingCategoryByUserId(userId);
        category.setName(name);
        categoryRepository.saveAndFlush(category);
    }

    @Override
    public void setType(String type, Long userId) {
        checkUser(userId);

        Category category = getCollectingCategoryByUserId(userId);
        category.setType(ETransaction.valueOf(type));
        categoryRepository.saveAndFlush(category);
    }

    @Override
    public void setCreatedAt(Long userId) {
        checkUser(userId);

        Category category = getCollectingCategoryByUserId(userId);
        category.setCreatedAt(Instant.now());
        categoryRepository.saveAndFlush(category);
    }

    private Category getCollectingCategoryByUserId(Long userId) {
        Category category;
        Optional<Category> categoryOpt = categoryRepository.findByUserIdAndCreatedAtIsNull(userId);
        if (categoryOpt.isPresent()) {
            category = categoryOpt.get();
        } else {
            category = new Category();
            category.setUserId(userId);
        }

        return category;
    }

    private void checkUser(Long userId) throws EntityNotFoundBotException {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundBotException("Не существует пользователя ID=".concat(String.valueOf(userId)));
        }
    }
}
