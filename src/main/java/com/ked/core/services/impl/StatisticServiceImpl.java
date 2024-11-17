package com.ked.core.services.impl;

import com.ked.core.dto.StatisticInfo;
import com.ked.core.dto.TransactionDto;
import com.ked.core.entities.Statistic;
import com.ked.core.enums.ETimeInterval;
import com.ked.core.mappers.TransactionMapper;
import com.ked.core.repositories.StatisticRepository;
import com.ked.core.repositories.TransactionRepository;
import com.ked.core.repositories.UserRepository;
import com.ked.core.services.StatisticService;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final StatisticRepository statisticRepository;

    private final UserRepository userRepository;

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
            default -> new ArrayList<>();
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
                .toList();
        ;
        return StatisticInfo.builder()
                .transactions(transactions)
                .build();
    }

    @Override
    public void setPeriod(String period, Long userId) {
        checkUser(userId);

        Statistic statistic = getCollectingStatisticByUserId(userId);
        statistic.setETimeInterval(ETimeInterval.valueOf(period));
        statisticRepository.saveAndFlush(statistic);
    }

    @Override
    public Statistic setCreatedAt(Long userId) {
        checkUser(userId);

        Statistic statistic = getCollectingStatisticByUserId(userId);
        statistic.setCreatedAt(new Date());
        return statisticRepository.saveAndFlush(statistic);
    }

    private Statistic getCollectingStatisticByUserId(Long userId) {
        Statistic statistic;
        Optional<Statistic> statisticOpt = statisticRepository.findByUserIdAndCreatedAtIsNull(userId);
        if (statisticOpt.isPresent()) {
            statistic = statisticOpt.get();
        } else {
            statistic = new Statistic();
            statistic.setUserId(userId);
        }

        return statistic;
    }

    private void checkUser(Long userId) throws EntityNotFoundBotException {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundBotException("Не существует пользователя ID=".concat(String.valueOf(userId)));
        }
    }
}
