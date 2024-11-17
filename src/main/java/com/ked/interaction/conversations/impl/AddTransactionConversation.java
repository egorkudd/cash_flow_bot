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
public class AddTransactionConversation extends AConversation {
    private final EConversation name = EConversation.ADD_TRANSACTION;

    private static final EConversationStep START_STEP = EConversationStep.TRANSACTION_TYPE_CHOICE;

    private static final String FINISH_MESSAGE = "Транзакция успешно добавлена!";

    public AddTransactionConversation() {
        super(completeStepGraph(), START_STEP, FINISH_MESSAGE);
    }

    private static Map<EConversationStep, List<EConversationStep>> completeStepGraph() {
        return new HashMap<>() {{
            put(EConversationStep.TRANSACTION_TYPE_CHOICE, new ArrayList<>() {{
                add(EConversationStep.TRANSACTION_CATEGORY_CHOICE);
            }});
            put(EConversationStep.TRANSACTION_CATEGORY_CHOICE, new ArrayList<>() {{
                add(EConversationStep.TRANSACTION_INPUT);
            }});
            put(EConversationStep.TRANSACTION_INPUT, new ArrayList<>() {{
                add(null);
            }});
        }};
    }
}
