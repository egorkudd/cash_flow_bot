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
public class StatisticConversation extends AConversation {
    private final EConversation name = EConversation.STATISTIC;

    private static final EConversationStep START_STEP = EConversationStep.STATISTIC_PERIOD_CHOICE;

    private static final String FINISH_MESSAGE = null;

    public StatisticConversation() {
        super(completeStepGraph(), START_STEP, FINISH_MESSAGE);
    }

    private static Map<EConversationStep, List<EConversationStep>> completeStepGraph() {
        return new HashMap<>() {{
            put(EConversationStep.STATISTIC_PERIOD_CHOICE, new ArrayList<>() {{
                add(null);
            }});
        }};
    }
}
