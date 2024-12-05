package com.ked.interaction.configs;

import com.ked.interaction.conversations.AConversation;
import com.ked.interaction.conversations.impl.AddTransactionConversation;
import com.ked.interaction.conversations.impl.ConfigureConversation;
import com.ked.interaction.conversations.impl.SendBotMessageConversation;
import com.ked.interaction.conversations.impl.StatisticConversation;
import com.ked.interaction.enums.EConversation;
import com.ked.interaction.enums.EConversationStep;
import com.ked.interaction.steps.ConversationStep;
import com.ked.interaction.steps.impl.user.add_transaction.TransactionCategoryChoiceStep;
import com.ked.interaction.steps.impl.user.add_transaction.TransactionInputStep;
import com.ked.interaction.steps.impl.user.add_transaction.TransactionTypeChoiceStep;
import com.ked.interaction.steps.impl.user.configure.category.CategoryChoiceStep;
import com.ked.interaction.steps.impl.user.configure.category.CategoryNameInputStep;
import com.ked.interaction.steps.impl.user.configure.category.CategoryTypeChoiceStep;
import com.ked.interaction.steps.impl.user.configure.user.ChangeUserNameInputStep;
import com.ked.interaction.steps.impl.user.configure.category.ConfigureCategoryActionChoiceStep;
import com.ked.interaction.steps.impl.user.configure.ConfigureChoiceStep;
import com.ked.interaction.steps.impl.user.configure.user.EmailInputStep;
import com.ked.interaction.steps.impl.user.configure.user.PasswordInputStep;
import com.ked.interaction.steps.impl.user.statistic.StatisticLastDateInputStep;
import com.ked.interaction.steps.impl.user.statistic.StatisticPeriodChoiceStep;
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
            @Autowired ConfigureConversation configureConversation,
            @Autowired StatisticConversation statisticConversation
    ) {
        return new HashMap<>() {{
            put(sendBotMessageConversation.getName(), sendBotMessageConversation);
            put(addTransactionConversation.getName(), addTransactionConversation);
            put(configureConversation.getName(), configureConversation);
            put(statisticConversation.getName(), statisticConversation);
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
            @Autowired TransactionCategoryChoiceStep transactionCategoryChoiceStep,
            @Autowired TransactionInputStep transactionInputStep,

            @Autowired ConfigureChoiceStep configureChoiceStep,

            @Autowired ConfigureCategoryActionChoiceStep configureCategoryActionChoiceStep,
            @Autowired CategoryTypeChoiceStep categoryTypeChoiceStep,
            @Autowired CategoryNameInputStep categoryNameInputStep,
            @Autowired CategoryChoiceStep categoryChoiceStep,

            @Autowired ChangeUserNameInputStep changeUserNameInputStep,

            @Autowired StatisticPeriodChoiceStep statisticPeriodChoiceStep,
            @Autowired StatisticLastDateInputStep statisticLastDateInputStep,

            @Autowired EmailInputStep emailInputStep,
            @Autowired PasswordInputStep passwordInputStep
    ) {
        return new HashMap<>() {{
            put(EConversationStep.BOT_MESSAGE_TEXT_INPUT, textInputStep);
            put(EConversationStep.BOT_MESSAGE_BUTTON_ADD_CHOICE, buttonAddChoiceStep);
            put(EConversationStep.BOT_MESSAGE_BUTTON_INPUT, buttonInputStep);
            put(EConversationStep.BOT_MESSAGE_SEND_CHOICE, sendBotMessageChoiceStep);
            put(EConversationStep.BOT_MESSAGE_TYPE_CHOICE, chatTypeChoiceStep);

            put(EConversationStep.TRANSACTION_TYPE_CHOICE, transactionTypeChoiceStep);
            put(EConversationStep.TRANSACTION_CATEGORY_CHOICE, transactionCategoryChoiceStep);
            put(EConversationStep.TRANSACTION_INPUT, transactionInputStep);

            put(EConversationStep.CONFIGURE_CHOICE, configureChoiceStep);

            put(EConversationStep.CONFIGURE_CATEGORY_ACTION_CHOICE, configureCategoryActionChoiceStep);
            put(EConversationStep.CATEGORY_TYPE_CHOICE, categoryTypeChoiceStep);
            put(EConversationStep.CATEGORY_NAME_INPUT, categoryNameInputStep);
            put(EConversationStep.CATEGORY_CHOICE, categoryChoiceStep);
            put(EConversationStep.CATEGORY_NEW_NAME_INPUT, categoryNameInputStep);

            put(EConversationStep.CHANGE_USERNAME_INPUT, changeUserNameInputStep);

            put(EConversationStep.STATISTIC_PERIOD_CHOICE, statisticPeriodChoiceStep);
            put(EConversationStep.STATISTIC_LAST_DATE_INPUT, statisticLastDateInputStep);

            put(EConversationStep.EMAIL_INPUT, emailInputStep);
            put(EConversationStep.PASSWORD_INPUT, passwordInputStep);
        }};
    }
}
