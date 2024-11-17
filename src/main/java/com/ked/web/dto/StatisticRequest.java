package com.ked.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
public class StatisticRequest {
    private Long userId;
    private Instant startDate;
    private Instant endDate;
}
