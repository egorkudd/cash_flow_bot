package com.ked.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StatisticRequest {
    private Long userId;
    private String startDateStr;
    private String endDateStr;
}
