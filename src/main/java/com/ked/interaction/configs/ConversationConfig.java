package com.ked.interaction.configs;

import com.ked.interaction.conversations.AConversation;
import com.ked.interaction.conversations.impl.AddTransactionConversation;
import com.ked.interaction.conversations.impl.ConfigureConversation;
import com.ked.interaction.conversations.impl.SendBotMessageConversation;
import com.ked.interaction.enums.EConversation;
import com.ked.interaction.enums.EConversationStep;
import com.ked.interaction.steps.ConversationStep;
import com.ked.interaction.steps.impl.user.add_transaction.TransactionCategoryChoice;
import com.ked.interaction.steps.impl.user.add_transaction.TransactionInputStep;
import com.ked.interaction.steps.impl.user.add_transaction.TransactionTypeChoiceStep;
import com.ked.interaction.steps.impl.user.configure.CategoryNameInputStep;
import com.ked.interaction.steps.impl.user.configure.CategoryTypeChoiceStep;
import com.ked.interaction.steps.impl.user.configure.ChangeUserNameInputStep;
import com.ked.interaction.steps.impl.user.configure.ConfigureCategoryActionChoiceStep;
import com.ked.interaction.steps.impl.user.configure.ConfigureChoiceStep;
import com.ked.interaction.steps.impl.writer.ButtonAddChoiceStep;
import com.ked.interaction.steps.impl.writer.ButtonInputStep;
import com.ked.interaction.steps.impl.writer.ChatTypeChoiceStep;
import com.ked.interaction.steps.impl.writer.SendBotMessageChoiceStep;
import com.ked.interaction.steps.impl.writer.TextInputStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConversationConfig {
    @Bean
    public Map<EConversation, AConversation> conversationMap(
            @Autowired SendBotMessageConversation sendBotMessageConversation,
            @Autowired AddTransactionConversation addTransactionConversation,
            @Autowired ConfigureConversation configureConversation
    ) {
        return new HashMap<>() {{
            put(sendBotMessageConversation.getName(), sendBotMessageConversation);
            put(addTransactionConversation.getName(), addTransactionConversation);
            put(configureConversation.getName(), configureConversation);
        }};
    }

    @Bean
    public Map<EConversationStep, ConversationStep> conversationStepMap(
            @Autowired TextInputStep textInputStep,
            @Autowired ButtonAddChoiceStep buttonAddChoiceStep,
            @Autowired ButtonInputStep buttonInputStep,
            @Autowired SendBotMessageChoiceStep sendBotMessageChoiceStep,
            @Autowired ChatTypeChoiceStep chatTypeChoiceStep,

            @Autowired TransactionTypeChoiceStep transactionTypeChoiceStep,
            @Autowired TransactionCategoryChoice transactionCategoryChoice,
            @Autowired TransactionInputStep transactionInputStep,

            @Autowired ConfigureChoiceStep configureChoiceStep,

            @Autowired ConfigureCategoryActionChoiceStep configureCategoryActionChoiceStep,
            @Autowired CategoryTypeChoiceStep categoryTypeChoiceStep,
            @Autowired CategoryNameInputStep categoryNameInputStep,

            @Autowired ChangeUserNameInputStep changeUserNameInputStep
            ) {
        return new HashMap<>() {{
            put(textInputStep.getName(), textInputStep);
            put(buttonAddChoiceStep.getName(), buttonAddChoiceStep);
            put(buttonInputStep.getName(), buttonInputStep);
            put(sendBotMessageChoiceStep.getName(), sendBotMessageChoiceStep);
            put(chatTypeChoiceStep.getName(), chatTypeChoiceStep);

            put(transactionTypeChoiceStep.getName(), transactionTypeChoiceStep);
            put(transactionCategoryChoice.getName(), transactionCategoryChoice);
            put(transactionInputStep.getName(), transactionInputStep);

            put(configureChoiceStep.getName(), configureChoiceStep);

            put(configureCategoryActionChoiceStep.getName(), configureCategoryActionChoiceStep);
            put(categoryTypeChoiceStep.getName(), categoryTypeChoiceStep);
            put(categoryNameInputStep.getName(), categoryNameInputStep);

            put(changeUserNameInputStep.getName(), changeUserNameInputStep);
        }};
    }
}
