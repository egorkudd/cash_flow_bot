package com.ked.interaction.conversations;


import com.ked.interaction.enums.EConversation;
import com.ked.interaction.enums.EConversationStep;

import java.util.List;
import java.util.Map;

public abstract class AConversation {
    private final Map<EConversationStep, List<EConversationStep>> STEP_GRAPH;
    private final EConversationStep START_STEP;

    private final String FINISH_MESSAGE_TEXT;

    protected AConversation(
            Map<EConversationStep, List<EConversationStep>> stepGraph,
            EConversationStep firstStep,
            String finishMessageText
    ) {
        STEP_GRAPH = stepGraph;
        START_STEP = firstStep;
        FINISH_MESSAGE_TEXT = finishMessageText;
    }

    public abstract EConversation getName();

    public EConversationStep getStartStep() {
        return START_STEP;
    }

    public EConversationStep getNextStep(EConversationStep prevStep, int stepIndex) {
        return STEP_GRAPH.get(prevStep).get(stepIndex);
    }

    public String getFinishMessageText() {
        return FINISH_MESSAGE_TEXT;
    }
}
