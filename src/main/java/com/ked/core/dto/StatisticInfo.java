package com.ked.core.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class StatisticInfo {
    List<TransactionDto> transactions;
}
