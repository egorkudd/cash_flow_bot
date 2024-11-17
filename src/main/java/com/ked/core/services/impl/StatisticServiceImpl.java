package com.ked.core.services.impl;

import com.ked.core.dto.StatisticInfo;
import com.ked.core.dto.TransactionDto;
import com.ked.core.entities.Statistic;
import com.ked.core.entities.Transaction;
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

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final StatisticRepository statisticRepository;

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper;

    private final ChartGenerator chartGenerator;

    @Override
    public StatisticInfo getTransactionStatisticByTimeInterval(Long userId, ETimeInterval interval, Instant dateTime) {
        List<TransactionDto> transactions = switch (interval) {
            case DAY -> getTransactionsByUserAndDay(userId, dateTime).stream()
                    .map(transactionMapper::toDto)
                    .toList();
            case WEEK -> getTransactionsByUserAndWeek(userId, dateTime).stream()
                    .map(transactionMapper::toDto)
                    .toList();
            case MONTH -> getTransactionsByUserAndDate(userId, dateTime).stream()
                    .map(transactionMapper::toDto)
                    .toList();
            case YEAR -> getTransactionsByUserAndYear(userId, dateTime).stream()
                    .map(transactionMapper::toDto)
                    .toList();
            default -> new ArrayList<>();
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
        List<TransactionDto> transactions = getTransactionsByUserAndDay(userId, startDate, endDate).stream()
                .map(transactionMapper::toDto)
                .toList();;

        Map<String, Double> categoryDistribution = calculateCategoryDistribution(transactions);

        return StatisticInfo.builder()
                .diagramPng(chartGenerator.generatePieChart(categoryDistribution))
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

    private Map<String, Double> calculateCategoryDistribution(List<TransactionDto> transactions) {
        if (transactions.isEmpty()) {
            return Map.of();
        }

        Map<String, BigDecimal> categorySums = transactions.stream()
                .collect(Collectors.groupingBy(TransactionDto::getCategoryName,
                        Collectors.mapping(TransactionDto::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

        BigDecimal totalSum = categorySums.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return categorySums.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> totalSum.compareTo(BigDecimal.ZERO) == 0 ? 0.0 : (entry.getValue().multiply(BigDecimal.valueOf(100.0)).divide(totalSum, BigDecimal.ROUND_HALF_UP)).doubleValue()
                ));
    }

    private List<Transaction> getTransactionsByUserAndDay(Long userId, Instant startDate, Instant endDate) {
        LocalDate startlocalDate = startDate.atZone(ZoneId.of("Europe/Moscow")).toLocalDate();
        LocalDate endlocalDate = endDate.atZone(ZoneId.of("Europe/Moscow")).toLocalDate();

        LocalDateTime dayOfMonthTime = startlocalDate.atStartOfDay();
        ZonedDateTime dayZonedDateTime = dayOfMonthTime.atZone(ZoneId.of("UTC"));
        Instant firstInstant = dayZonedDateTime.toInstant();

        LocalDateTime dayEndOfMonthTime = endlocalDate.atTime(23, 59, 59, 999_999_999);
        ZonedDateTime dayEndZonedDateTime = dayEndOfMonthTime.atZone(ZoneId.of("UTC"));
        Instant secondInstant = dayEndZonedDateTime.toInstant();

        return transactionRepository.findByUserIdAndCreatedAtBetween(userId, firstInstant, secondInstant);
    }

    private List<Transaction> getTransactionsByUserAndDay(Long userId, Instant date) {
        LocalDate localDate = date.atZone(ZoneId.of("Europe/Moscow")).toLocalDate();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        int day = localDate.getDayOfMonth();

        LocalDate dayLocalDate = LocalDate.of(year, month, day);
        LocalDateTime dayOfMonthTime = dayLocalDate.atStartOfDay();
        ZonedDateTime dayZonedDateTime = dayOfMonthTime.atZone(ZoneId.of("UTC"));
        Instant firstInstant = dayZonedDateTime.toInstant();

        LocalDate dayEndLocalDate = LocalDate.of(year, month, day);
        LocalDateTime dayEndOfMonthTime = dayEndLocalDate.atTime(23, 59, 59, 999_999_999);
        ZonedDateTime dayEndZonedDateTime = dayEndOfMonthTime.atZone(ZoneId.of("UTC"));
        Instant secondInstant = dayEndZonedDateTime.toInstant();

        return transactionRepository.findByUserIdAndCreatedAtBetween(userId, firstInstant, secondInstant);
    }

    private List<Transaction> getTransactionsByUserAndWeek(Long userId, Instant date) {
        LocalDate localDate = date.atZone(ZoneId.of("Europe/Moscow")).toLocalDate();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        int day = localDate.getDayOfMonth();

        LocalDate dayLocalDate = LocalDate.of(year, month, day);
        LocalDateTime dayOfMonthTime = dayLocalDate.atStartOfDay().minusDays(6);
        ZonedDateTime dayZonedDateTime = dayOfMonthTime.atZone(ZoneId.of("UTC"));
        Instant firstInstant = dayZonedDateTime.toInstant();

        LocalDate dayEndLocalDate = LocalDate.of(year, month, day);
        LocalDateTime dayEndOfMonthTime = dayEndLocalDate.atTime(23, 59, 59, 999_999_999);
        ;
        ZonedDateTime dayEndZonedDateTime = dayEndOfMonthTime.atZone(ZoneId.of("UTC"));
        Instant secondInstant = dayEndZonedDateTime.toInstant();

        return transactionRepository.findByUserIdAndCreatedAtBetween(userId, firstInstant, secondInstant);
    }

    private List<Transaction> getTransactionsByUserAndDate(Long userId, Instant date) {
        LocalDate localDate = date.atZone(ZoneId.of("Europe/Moscow")).toLocalDate();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDateTime firstDayOfMonthTime = firstDayOfMonth.atStartOfDay();
        ZonedDateTime firstZonedDateTime = firstDayOfMonthTime.atZone(ZoneId.of("UTC"));
        Instant firstInstant = firstZonedDateTime.toInstant();

        LocalDate lastDayOfMonth = LocalDate.of(year, month, firstDayOfMonth.lengthOfMonth());
        LocalDateTime lastDayOfMonthTime = lastDayOfMonth.atTime(23, 59, 59, 999_999_999);
        ZonedDateTime lastZonedDateTime = lastDayOfMonthTime.atZone(ZoneId.of("UTC"));
        Instant lastInstant = lastZonedDateTime.toInstant();

        return transactionRepository.findByUserIdAndMonth(userId, firstInstant, lastInstant);
    }

    private List<Transaction> getTransactionsByUserAndYear(Long userId, Instant date) {

        LocalDate localDate = date.atZone(ZoneId.of("Europe/Moscow")).toLocalDate();
        int year = localDate.getYear();
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        LocalDateTime firstDayOfYearTime = firstDayOfYear.atStartOfDay();
        ZonedDateTime firstZonedDateTime = firstDayOfYearTime.atZone(ZoneId.of("UTC"));
        Instant firstInstant = firstZonedDateTime.toInstant();

        LocalDate lastDayOfYear = LocalDate.of(year, 12, 31);
        LocalDateTime lastDayOfYearTime = lastDayOfYear.atTime(23, 59, 59, 999_999_999); // Устанавливаем время на конец дня
        ZonedDateTime lastZonedDateTime = lastDayOfYearTime.atZone(ZoneId.of("UTC"));
        Instant lastInstant = lastZonedDateTime.toInstant();

        return transactionRepository.findByUserIdAndYear(userId, firstInstant, lastInstant);
    }
}
