package com.ked.core.services;

import com.ked.core.dto.TransactionDto;
import com.ked.core.entities.Transaction;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface TransactionService {
    Transaction create(TransactionDto dto);

    List<Transaction> findByDate(Date startDate, Date endDate);

    Transaction update(Long id, TransactionDto dto);

    boolean delete(Long id);

    void setType(String type, Long userId) throws EntityNotFoundBotException;

    void setCategory(String categoryIdStr, Long userId) throws EntityNotFoundBotException;

    void setAmount(String amount, Long userId) throws EntityNotFoundBotException;

    void setCreatedAt(Long userId);
}
