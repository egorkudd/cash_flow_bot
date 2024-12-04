package com.ked.web.controllers;

import com.ked.core.services.UserService;
import com.ked.tg.bots.MyLongPoolingBot;
import com.ked.tg.utils.MessageUtil;
import com.ked.web.dto.RegularTransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReminderController {
    private final MyLongPoolingBot bot;

    private final UserService userService;

    @PostMapping("/reminder")
    public HttpStatus getTransactionListBetweenDates(@RequestBody RegularTransactionDto dto) {
        MessageUtil.sendMessage(
                userService.findById(dto.getUserId()).getTgId(),
                dto.toString(),
                bot
        );

        return HttpStatus.ACCEPTED;
    }
}
