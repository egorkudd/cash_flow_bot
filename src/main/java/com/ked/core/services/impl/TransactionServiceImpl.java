package com.ked.core.services.impl;

import com.ked.core.dto.TransactionDto;
import com.ked.core.entities.Transaction;
import com.ked.core.enums.ETransaction;
import com.ked.core.repositories.CategoryRepository;
import com.ked.core.repositories.TransactionRepository;
import com.ked.core.repositories.UserRepository;
import com.ked.core.services.TransactionService;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Transaction create(TransactionDto dto) {
        return null;
    }

    @Override
    public List<Transaction> findByDate(Date startDate, Date endDate) {
        return null;
    }

    @Override
    public Transaction update(Long id, TransactionDto dto) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public void setType(String type, Long userId) throws EntityNotFoundBotException {
        checkUser(userId);

        Transaction transaction = getCollectingTransactionByUserId(userId);
        transaction.setType(ETransaction.valueOf(type));
        transactionRepository.saveAndFlush(transaction);
    }

    @Override
    public void setCategory(String categoryIdStr, Long userId) throws EntityNotFoundBotException {
        Long categoryId = Long.parseLong(categoryIdStr);

        checkUser(userId);
        checkCategory(categoryId);

        Transaction transaction = getCollectingTransactionByUserId(userId);
        transaction.setCategoryId(categoryId);
        transactionRepository.saveAndFlush(transaction);
    }

    @Override
    public void setAmount(String amount, Long userId) throws EntityNotFoundBotException {
        checkUser(userId);

        Transaction transaction = getCollectingTransactionByUserId(userId);
        transaction.setAmount(new BigDecimal(amount));
        transactionRepository.saveAndFlush(transaction);
    }

    @Override
    public void setCreatedAt(Long userId) {
        checkUser(userId);

        Transaction transaction = getCollectingTransactionByUserId(userId);
        transaction.setCreatedAt(LocalDateTime.now());
        transactionRepository.saveAndFlush(transaction);
    }

    private Transaction getCollectingTransactionByUserId(Long userId) {
        Transaction transaction;
        Optional<Transaction> transactionOpt = transactionRepository.findByUserIdAndCreatedAtIsNull(userId);
        if (transactionOpt.isPresent()) {
            transaction = transactionOpt.get();
        } else {
            transaction = new Transaction();
            transaction.setUserId(userId);
        }

        return transaction;
    }

    private void checkUser(Long userId) throws EntityNotFoundBotException {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundBotException("Не существует пользователя ID=".concat(String.valueOf(userId)));
        }
    }

    private void checkCategory(Long categoryId) throws EntityNotFoundBotException {
        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundBotException("Не существует категории ID=".concat(String.valueOf(categoryId)));
        }
    }
}
