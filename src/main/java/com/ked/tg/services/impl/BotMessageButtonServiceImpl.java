package com.ked.tg.services.impl;

import com.ked.tg.entities.BotMessageButton;
import com.ked.tg.mappers.BotMessageButtonMapper;
import com.ked.tg.repositories.BotMessageButtonRepository;
import com.ked.tg.services.BotMessageButtonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BotMessageButtonServiceImpl implements BotMessageButtonService {
    private final BotMessageButtonRepository botMessageButtonRepository;
    private final BotMessageButtonMapper botMessageButtonMapper;

    @Override
    public BotMessageButton create(long botMessageId, String buttonName, String buttonLink) {
        BotMessageButton botMessageButton = botMessageButtonMapper.botMessageButton(
                botMessageId, buttonName, buttonLink
        );
        return botMessageButtonRepository.saveAndFlush(botMessageButton);
    }

    @Override
    public List<BotMessageButton> getListByMessageId(long botMessageId) {
        return botMessageButtonRepository.findAllByBotMessageId(botMessageId);
    }

    @Override
    public void deleteButtons(long botMessageId) {
        botMessageButtonRepository.deleteAllByBotMessageId(botMessageId);
    }
}
