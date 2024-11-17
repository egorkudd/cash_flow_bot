package com.ked.core.services.impl;

import com.ked.core.dto.StatisticInfo;
import com.ked.core.dto.TransactionDto;
import com.ked.core.enums.ETimeInterval;
import com.ked.core.mappers.TransactionMapper;
import com.ked.core.repositories.TransactionRepository;
import com.ked.core.services.StatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final ChartGenerator chartGenerator;

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

        log.info("Transactions: {}", transactions);

        Map<String, Double> categoryDistribution = calculateCategoryDistribution(transactions);

        log.info("Category Distribution: {}", categoryDistribution);

        return StatisticInfo.builder()
                .diagramPng(chartGenerator.generatePieChart(categoryDistribution))
                .transactions(transactions)
                .build();
    }


    @Override
    public StatisticInfo getTransactionStatisticByCustomTimeInterval(Long userId, Instant startDate, Instant endDate) {
        List<TransactionDto> transactions = transactionRepository
                .findByUserIdAndCreatedAtBetween(userId, startDate, endDate).stream()
                .map(transactionMapper::toDto)
                .toList();;

        Map<String, Double> categoryDistribution = calculateCategoryDistribution(transactions);

        return StatisticInfo.builder()
                .diagramPng(chartGenerator.generatePieChart(categoryDistribution))
                .transactions(transactions)
                .build();
    }

    private Map<String, Double> calculateCategoryDistribution(List<TransactionDto> transactions) {
        if (transactions.isEmpty()) {
            return Map.of();
        }

        long totalTransactions = transactions.size();

        Map<String, Long> categoryCounts = transactions.stream()
                .collect(Collectors.groupingBy(TransactionDto::getCategoryName, Collectors.counting()));

        return categoryCounts.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (entry.getValue() * 100.0) / totalTransactions
                ));
    }
}
