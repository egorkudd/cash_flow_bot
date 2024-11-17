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
public class ConfigureConversation extends AConversation {
    private final EConversation name = EConversation.CONFIGURE;

    private static final EConversationStep START_STEP = EConversationStep.CONFIGURE_CHOICE;

    private static final String FINISH_MESSAGE = "Изменения успешно сохранены!";

    public ConfigureConversation() {
        super(completeStepGraph(), START_STEP, FINISH_MESSAGE);
    }

    private static Map<EConversationStep, List<EConversationStep>> completeStepGraph() {
        return new HashMap<>() {{
            put(EConversationStep.CONFIGURE_CHOICE, new ArrayList<>() {{
                add(EConversationStep.CHANGE_USERNAME_INPUT);
                add(EConversationStep.CONFIGURE_CATEGORY_ACTION_CHOICE);
            }});
            put(EConversationStep.CHANGE_USERNAME_INPUT, new ArrayList<>() {{
                add(null);
            }});
            put(EConversationStep.CONFIGURE_CATEGORY_ACTION_CHOICE, new ArrayList<>() {{
                add(EConversationStep.CATEGORY_TYPE_CHOICE);
                add(EConversationStep.CATEGORY_NEW_NAME_INPUT);
            }});
            put(EConversationStep.CATEGORY_TYPE_CHOICE, new ArrayList<>() {{
                add(EConversationStep.CATEGORY_NAME_INPUT);
            }});
            put(EConversationStep.CATEGORY_NAME_INPUT, new ArrayList<>() {{
                add(null);
            }});
            put(EConversationStep.CATEGORY_NEW_NAME_INPUT, new ArrayList<>() {{
                add(null);
            }});
        }};
    }
}

