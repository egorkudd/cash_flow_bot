package com.ked.interaction.conversations.impl;

import com.ked.interaction.conversations.AConversation;
import com.ked.interaction.enums.EConversation;
import com.ked.interaction.enums.EConversationStep;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Component
public class SendBotMessageConversation extends AConversation {
    private final EConversation name = EConversation.SEND_BOT_MESSAGE;

    private static final EConversationStep START_STEP = EConversationStep.BOT_MESSAGE_TEXT_INPUT;

    private static final String FINISH_MESSAGE = "Сообщение отправлено";

    public SendBotMessageConversation() {
        super(completeStepGraph(), START_STEP, FINISH_MESSAGE);
    }

    private static Map<EConversationStep, List<EConversationStep>> completeStepGraph() {
        return new HashMap<>() {{
            put(EConversationStep.BOT_MESSAGE_TEXT_INPUT, new ArrayList<>() {{
                add(EConversationStep.BOT_MESSAGE_BUTTON_ADD_CHOICE);
            }});
            put(EConversationStep.BOT_MESSAGE_BUTTON_ADD_CHOICE, new ArrayList<>() {{
                add(EConversationStep.BOT_MESSAGE_BUTTON_INPUT);
                add(EConversationStep.BOT_MESSAGE_TYPE_CHOICE);
            }});
            put(EConversationStep.BOT_MESSAGE_BUTTON_INPUT, new ArrayList<>() {{
                add(EConversationStep.BOT_MESSAGE_BUTTON_ADD_CHOICE);
            }});
            put(EConversationStep.BOT_MESSAGE_TYPE_CHOICE, new ArrayList<>() {{
                add(EConversationStep.BOT_MESSAGE_SEND_CHOICE);
            }});
            put(EConversationStep.BOT_MESSAGE_SEND_CHOICE, new ArrayList<>() {{
                add(null);
            }});
        }};
    }
}
