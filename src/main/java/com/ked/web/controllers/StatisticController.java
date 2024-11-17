package com.ked.web.controllers;

import com.ked.core.dto.TransactionDto;
import com.ked.core.services.StatisticService;
import com.ked.tg.utils.DateUtil;
import com.ked.web.dto.StatisticRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @PostMapping("/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactionListBetweenDates(@RequestBody StatisticRequest request) {
        return ResponseEntity.ok(
                statisticService.getTransactionStatisticByCustomTimeInterval(
                        request.getUserId(), request.getStartDate(), request.getEndDate()
                ).getTransactions()
        );
    }
}
