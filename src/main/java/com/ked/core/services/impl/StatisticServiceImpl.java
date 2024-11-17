package com.ked.core.services.impl;

import com.ked.core.dto.StatisticInfo;
import com.ked.core.dto.TransactionDto;
import com.ked.core.entities.Transaction;
import com.ked.core.enums.ETimeInterval;
import com.ked.core.mappers.TransactionMapper;
import com.ked.core.repositories.TransactionRepository;
import com.ked.core.services.StatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public StatisticInfo getTransactionStatisticByTimeInterval(Long userId, ETimeInterval interval, Instant dateTime) {

        List<TransactionDto> transactions = switch (interval) {
            case DAY -> transactionRepository.findByUserIdAndCreatedAt(userId, dateTime).stream()
                    .map(transactionMapper::toDto)
                    .toList();
            case WEEK -> transactionRepository.findByUserIdAndCreatedAtBetween(
                            userId,
                            dateTime.minusSeconds(6 * 24 * 60 * 60),
                            dateTime
                    ).stream()
                    .map(transactionMapper::toDto)
                    .toList();
            case MONTH -> transactionRepository.findByUserIdAndMonth(userId, dateTime).stream()
                    .map(transactionMapper::toDto)
                    .toList();
            case YEAR -> transactionRepository.findByUserIdAndYear(userId, dateTime).stream()
                    .map(transactionMapper::toDto)
                    .toList();
        };

        return StatisticInfo.builder()
                .transactions(transactions)
                .build();
    }

    @Override
    public StatisticInfo getTransactionStatisticByCustomTimeInterval(Long userId, Instant startDate, Instant endDate) {
        List<TransactionDto> transactions = transactionRepository
                .findByUserIdAndCreatedAtBetween(userId, startDate, endDate).stream()
                .map(transactionMapper::toDto)
                .toList();;
        return StatisticInfo.builder().build();
    }
}
