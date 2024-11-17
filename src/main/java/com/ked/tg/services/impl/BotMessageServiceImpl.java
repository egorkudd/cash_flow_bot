package com.ked.tg.services.impl;

import com.ked.tg.entities.BotMessage;
import com.ked.tg.entities.BotMessageButton;
import com.ked.tg.enums.EBotMessage;
import com.ked.tg.enums.EChat;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import com.ked.tg.mappers.BotMessageMapper;
import com.ked.tg.repositories.BotMessageRepository;
import com.ked.tg.services.BotMessageButtonService;
import com.ked.tg.services.BotMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BotMessageServiceImpl implements BotMessageService {
    private final BotMessageButtonService botMessageButtonService;
    private final BotMessageRepository botMessageRepository;
    private final BotMessageMapper botMessageMapper;

    @Override
    public void create(long botUserId) {
        BotMessage botMessage = botMessageMapper.botMessage(botUserId);
        botMessageRepository.saveAndFlush(botMessage);
    }

    @Override
    public BotMessage getProcessedMessageByUserId(long botUserId) throws EntityNotFoundBotException {
        return botMessageRepository.findByWriterIdAndStatus(botUserId, EBotMessage.WRITING)
                .orElseThrow(() -> new EntityNotFoundBotException(
                        "Не существует сообщения с writerId = %d".formatted(botUserId),
                        "Не достаточно прав"
                ));
    }

    @Override
    public void saveText(long botUserId, String text) throws EntityNotFoundBotException {
        BotMessage botMessage = getProcessedMessageByUserId(botUserId);
        botMessage.setText(text);
        botMessageRepository.saveAndFlush(botMessage);
    }

    @Override
    public void saveSentStatus(BotMessage botMessage) {
        botMessage.setStatus(EBotMessage.SENT);
        botMessageRepository.saveAndFlush(botMessage);
    }

    @Override
    public void delete(BotMessage botMessage) {
        botMessageRepository.delete(botMessage);
    }

    @Override
    public void createButton(long botUserId, String buttonName, String buttonLink) throws EntityNotFoundBotException {
        BotMessage botMessage = getProcessedMessageByUserId(botUserId);
        botMessageButtonService.create(botMessage.getId(), buttonName, buttonLink);
    }

    @Override
    public List<BotMessageButton> getButtonList(BotMessage botMessage) {
        return botMessageButtonService.getListByMessageId(botMessage.getId());
    }

    @Override
    public void deleteButtons(BotMessage botMessage) {
        botMessageButtonService.deleteButtons(botMessage.getId());
    }

    @Override
    public void saveChatType(long userId, EChat eChat) throws EntityNotFoundBotException {
        BotMessage botMessage = getProcessedMessageByUserId(userId);
        botMessage.setEChat(eChat);
        botMessageRepository.saveAndFlush(botMessage);
    }
}
