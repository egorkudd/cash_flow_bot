package com.ked.core.services.impl;

import com.ked.core.dto.StatisticInfo;
import com.ked.core.dto.TransactionDto;
import com.ked.core.entities.Statistic;
import com.ked.core.entities.Transaction;
import com.ked.core.enums.ETimeInterval;
import com.ked.core.enums.ETransaction;
import com.ked.core.mappers.TransactionMapper;
import com.ked.core.repositories.StatisticRepository;
import com.ked.core.repositories.TransactionRepository;
import com.ked.core.repositories.UserRepository;
import com.ked.core.services.StatisticService;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

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
            case TODAY -> getTransactionsByUserAndDay(userId, dateTime).stream()
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
            case EXIT -> null;
        };

        Map<String, Double> categoryDistribution = calculateCategoryDistribution(transactions);

        return StatisticInfo.builder()
                .diagramPng(chartGenerator.generatePieChart(categoryDistribution))
                .transactions(transactions)
                .build();
    }

    @Override
    public StatisticInfo getTransactionStatisticByCustomTimeInterval(Long userId, Instant startDate, Instant endDate) {
        List<TransactionDto> transactions = getTransactionsByUserAndDay(userId, startDate, endDate).stream()
                .map(transactionMapper::toDto)
                .toList();
        ;

        Map<String, Double> categoryDistribution = calculateCategoryDistribution(transactions);

        return StatisticInfo.builder()
                .diagramPng(chartGenerator.generatePieChart(categoryDistribution))
                .transactions(transactions)
                .build();
    }

    @Override
    public String collectStatisticMessage(StatisticInfo info, ETimeInterval eTimeInterval) {
        StringJoiner incomeJoiner = new StringJoiner("\n    ");
        StringJoiner expenseJoiner = new StringJoiner("\n    ");

        Map<Long, String> categoryMap = new HashMap<>();
        info.getTransactions().forEach(t -> categoryMap.putIfAbsent(t.getCategoryId(), t.getCategoryName()));

        info.getTransactions().stream()
                .collect(Collectors.groupingBy(TransactionDto::getCategoryId))
                .forEach((k, v) -> {
                    BigDecimal cost = v.stream()
                            .map(TransactionDto::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    if (ETransaction.INCOME.equals(v.get(0).getETransaction())) {
                        incomeJoiner.add("%s - %s RUB".formatted(categoryMap.get(k), cost));
                    } else {
                        expenseJoiner.add("%s - %s RUB".formatted(categoryMap.get(k), cost));
                    }
                });


        return """
                Ваша статистика за %s:
                <b>Доходы:</b>
                    %s
                <b>Расходы:</b>
                    %s
                """.formatted(eTimeInterval.getValue(), incomeJoiner, expenseJoiner);
    }

    @Override
    public void setPeriod(String period, Long userId) {
        checkUser(userId);

        Statistic statistic = getCollectingStatisticByUserId(userId);
        statistic.setETimeInterval(ETimeInterval.valueOf(period));
        statisticRepository.saveAndFlush(statistic);
    }

    @Override
    public void setYear(String yearStr, Long userId) {
        checkUser(userId);

        Statistic statistic = getCollectingStatisticByUserId(userId);
        statistic.setYear(Integer.parseInt(yearStr));
        statisticRepository.saveAndFlush(statistic);
    }

    @Override
    public void setMonth(String monthStr, Long userId) {
        checkUser(userId);

        Statistic statistic = getCollectingStatisticByUserId(userId);
        statistic.setMonth(Integer.parseInt(monthStr));
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
        ZonedDateTime dayZonedDateTime = dayOfMonthTime.atZone(ZoneId.of("Europe/Moscow"));
        LocalDateTime first = dayZonedDateTime.toLocalDateTime();

        LocalDateTime dayEndOfMonthTime = endlocalDate.atTime(23, 59, 59, 999_999_999);
        ZonedDateTime dayEndZonedDateTime = dayEndOfMonthTime.atZone(ZoneId.of("Europe/Moscow"));
        LocalDateTime second = dayEndZonedDateTime.toLocalDateTime();

        return transactionRepository.findByUserIdAndCreatedAtBetween(userId, first, second);
    }

    private List<Transaction> getTransactionsByUserAndDay(Long userId, Instant date) {
        LocalDate localDate = date.atZone(ZoneId.of("Europe/Moscow")).toLocalDate();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        int day = localDate.getDayOfMonth();

        LocalDate dayLocalDate = LocalDate.of(year, month, day);
        LocalDateTime dayOfMonthTime = dayLocalDate.atStartOfDay();
        ZonedDateTime dayZonedDateTime = dayOfMonthTime.atZone(ZoneId.of("Europe/Moscow"));
        LocalDateTime first = dayZonedDateTime.toLocalDateTime();

        LocalDate dayEndLocalDate = LocalDate.of(year, month, day);
        LocalDateTime dayEndOfMonthTime = dayEndLocalDate.atTime(23, 59, 59, 999_999_999);
        ZonedDateTime dayEndZonedDateTime = dayEndOfMonthTime.atZone(ZoneId.of("Europe/Moscow"));
        LocalDateTime second = dayEndZonedDateTime.toLocalDateTime();

        return transactionRepository.findByUserIdAndCreatedAtBetween(userId, first, second);
    }

    private List<Transaction> getTransactionsByUserAndWeek(Long userId, Instant date) {
        LocalDate localDate = date.atZone(ZoneId.of("Europe/Moscow")).toLocalDate();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        int day = localDate.getDayOfMonth();

        LocalDate dayLocalDate = LocalDate.of(year, month, day);
        LocalDateTime dayOfMonthTime = dayLocalDate.atStartOfDay().minusDays(6);
        ZonedDateTime dayZonedDateTime = dayOfMonthTime.atZone(ZoneId.of("Europe/Moscow"));
        LocalDateTime first = dayZonedDateTime.toLocalDateTime();

        LocalDate dayEndLocalDate = LocalDate.of(year, month, day);
        LocalDateTime dayEndOfMonthTime = dayEndLocalDate.atTime(23, 59, 59, 999_999_999);

        ZonedDateTime dayEndZonedDateTime = dayEndOfMonthTime.atZone(ZoneId.of("Europe/Moscow"));
        LocalDateTime second = dayEndZonedDateTime.toLocalDateTime();

        return transactionRepository.findByUserIdAndCreatedAtBetween(userId, first, second);
    }

    private List<Transaction> getTransactionsByUserAndDate(Long userId, Instant date) {
        LocalDate localDate = date.atZone(ZoneId.of("Europe/Moscow")).toLocalDate();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDateTime firstDayOfMonthTime = firstDayOfMonth.atStartOfDay();
        ZonedDateTime firstZonedDateTime = firstDayOfMonthTime.atZone(ZoneId.of("Europe/Moscow"));
        LocalDateTime first = firstZonedDateTime.toLocalDateTime();

        LocalDate lastDayOfMonth = LocalDate.of(year, month, firstDayOfMonth.lengthOfMonth());
        LocalDateTime lastDayOfMonthTime = lastDayOfMonth.atTime(23, 59, 59, 999_999_999);
        ZonedDateTime lastZonedDateTime = lastDayOfMonthTime.atZone(ZoneId.of("Europe/Moscow"));
        LocalDateTime last = lastZonedDateTime.toLocalDateTime();

        return transactionRepository.findByUserIdAndMonth(userId, first, last);
    }

    private List<Transaction> getTransactionsByUserAndYear(Long userId, Instant date) {

        LocalDate localDate = date.atZone(ZoneId.of("Europe/Moscow")).toLocalDate();
        int year = localDate.getYear();
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        LocalDateTime firstDayOfYearTime = firstDayOfYear.atStartOfDay();
        ZonedDateTime firstZonedDateTime = firstDayOfYearTime.atZone(ZoneId.of("Europe/Moscow"));
        LocalDateTime first = firstZonedDateTime.toLocalDateTime();

        LocalDate lastDayOfYear = LocalDate.of(year, 12, 31);
        LocalDateTime lastDayOfYearTime = lastDayOfYear.atTime(23, 59, 59, 999_999_999);
        ZonedDateTime lastZonedDateTime = lastDayOfYearTime.atZone(ZoneId.of("Europe/Moscow"));
        LocalDateTime last = lastZonedDateTime.toLocalDateTime();

        return transactionRepository.findByUserIdAndYear(userId, first, last);
    }
}
