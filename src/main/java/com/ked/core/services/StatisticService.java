package com.ked.core.services;

import com.ked.core.dto.StatisticInfo;
import com.ked.core.entities.Statistic;
import com.ked.core.enums.ETimeInterval;
import org.springframework.stereotype.Service;


import java.time.Instant;

@Service
public interface StatisticService {
    StatisticInfo getTransactionStatisticByTimeInterval(Long userId, ETimeInterval interval, Instant dateTime);

    StatisticInfo getTransactionStatisticByCustomTimeInterval(Long userId, Instant startDate, Instant endDate);

    void setPeriod(String period, Long userId);

    Statistic setCreatedAt(Long userId);
}
