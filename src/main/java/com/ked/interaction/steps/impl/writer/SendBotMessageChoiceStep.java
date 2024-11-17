package com.ked.interaction.steps.impl.writer;

import com.ked.interaction.steps.ChoiceStep;
import com.ked.tg.builders.MessageBuilder;
import com.ked.tg.dto.ButtonDto;
import com.ked.tg.dto.MessageDto;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.BotMessage;
import com.ked.tg.entities.BotMessageButton;
import com.ked.tg.entities.BotUser;
import com.ked.tg.entities.TgChat;
import com.ked.tg.enums.ERole;
import com.ked.tg.enums.EYesNo;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import com.ked.tg.mappers.KeyboardMapper;
import com.ked.tg.services.BotMessageService;
import com.ked.tg.services.BotUserService;
import com.ked.tg.services.GroupChatService;
import com.ked.tg.utils.ButtonUtil;
import com.ked.tg.utils.MessageUtil;
import com.ked.tg.utils.StepUtil;
import com.ked.tg.utils.ValidUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SendBotMessageChoiceStep extends ChoiceStep {
    private final BotMessageService botMessageService;

    private final BotUserService botUserService;

    private final KeyboardMapper keyboardMapper;

    private final GroupChatService groupChatService;

    private static final String PREPARE_MESSAGE_TEXT = "Ваше сообщение:";

    private static final String CHOICE_MESSAGE_TEXT = "Вы хотите отправить данное сообщение?";

    @Override
    protected ResultDto isValidData(MessageDto messageDto) throws EntityNotFoundBotException {
        return ValidUtil.isValidYesNoChoice(messageDto, EXCEPTION_MESSAGE_TEXT);
    }

    @Override
    public void prepare(TgChat tgChat, AbsSender sender) throws EntityNotFoundBotException {
        StepUtil.sendPrepareMessageOnlyText(tgChat, PREPARE_MESSAGE_TEXT, sender);
        sendMessageToCheck(tgChat, sender);
        StepUtil.sendPrepareMessageWithInlineKeyBoard(
                tgChat,
                CHOICE_MESSAGE_TEXT,
                keyboardMapper.keyboardDto(tgChat, ButtonUtil.yesNoButtonList()),
                sender
        );
    }

    @Override
    protected int finishStep(TgChat tgChat, AbsSender sender, String data) throws EntityNotFoundBotException {
        long userId = botUserService.getByChatIdAndRole(tgChat.getChatId(), ERole.ROLE_WRITER).getId();
        BotMessage botMessage = botMessageService.getProcessedMessageByUserId(userId);

        if (EYesNo.NO.toString().equals(data)) {
            botMessageService.deleteButtons(botMessage);
            botMessageService.delete(botMessage);
        } else {
            sendMessages(botMessage, tgChat, sender);
        }

        return 0;
    }

    private void sendMessageToCheck(TgChat tgChat, AbsSender sender) throws EntityNotFoundBotException {
        long userId = botUserService.getByChatIdAndRole(tgChat.getChatId(), ERole.ROLE_WRITER).getId();
        BotMessage botMessage = botMessageService.getProcessedMessageByUserId(userId);

        StepUtil.sendPrepareMessageWithInlineKeyBoard(
                tgChat,
                botMessage.getText(),
                keyboardMapper.keyboardDto(tgChat, collectButtonList(botMessage)),
                sender
        );
    }

    private List<ButtonDto> collectButtonList(BotMessage botMessage) {
        List<BotMessageButton> botMessageButtonList = botMessageService.getButtonList(botMessage);
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (BotMessageButton button : botMessageButtonList) {
            buttonDtoList.add(new ButtonDto(button.getButtonName(), button.getButtonName(), button.getButtonLink()));
        }

        return buttonDtoList;
    }

    private void sendMessages(BotMessage botMessage, TgChat tgChat, AbsSender sender) {
        MessageBuilder messageBuilder = MessageBuilder.create()
                .setText(botMessage.getText())
                .setInlineKeyBoard(keyboardMapper.keyboardDto(tgChat, collectButtonList(botMessage)));

        sendMessageToGroups(messageBuilder, sender);

        switch (botMessage.getEChat()) {
            case GROUP -> sendMessageToGroups(messageBuilder, sender);
            default -> sendMessageToUsers(messageBuilder, sender);
        }


        botMessageService.saveSentStatus(botMessage);
    }

    private void sendMessageToUsers(MessageBuilder messageBuilder, AbsSender sender) {
        botUserService.findAll().forEach(volunteer -> sendMessageToUser(messageBuilder, volunteer, sender));
        botUserService.flush();
    }

    private void sendMessageToUser(MessageBuilder messageBuilder, BotUser botUser, AbsSender sender) {
        Message message = sendMessage(messageBuilder, botUser.getTgId(), sender);
    }

    private void sendMessageToGroups(MessageBuilder messageBuilder, AbsSender sender) {
        groupChatService.findAll().forEach(groupChat -> sendMessage(messageBuilder, groupChat.getChatId(), sender));
    }

    private Message sendMessage(MessageBuilder messageBuilder, long chatId, AbsSender sender) {
        return MessageUtil.sendMessage(
                messageBuilder.sendMessage(chatId), sender
        );
    }
}
