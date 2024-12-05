package com.ked.interaction.commands;

import com.ked.core.enums.ETransaction;
import com.ked.core.services.CategoryService;
import com.ked.core.services.UserService;
import com.ked.tg.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class StartCommand extends BotCommand {
    private static final String DEFAULT_CATEGORY = "Без категории";

    private final UserService userService;

    private final CategoryService categoryService;

    private static final String HELLO_MESSAGE = """
            ☀️ Привет, дорогой друг!
            💸 Я – бот! И я помогу тебе с учетом твоих финансов
            
            ⚙️ Меню с командами расположено в левом нижнем углу
            
            ✅ Чтобы добавить категорию доходов или трат нажмите /configure
            ✅ Чтобы добавить доход или трату нажмите /add_transaction
            ✅ Чтобы добавить увидеть свою статистику нажмите /statistic
            
            👨‍💻 Остальные возможности вы сможете посмотреть, нажав /info
            """;

    public StartCommand(UserService userService, CategoryService categoryService) {
        super("start", "Start command");
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        createUser(chat.getId(), user.getFirstName());
        sendHelloMessage(chat.getId(), absSender);
    }

    private void createUser(long chatId, String name) {
        if (!userService.existsByTgId(chatId)) {
            com.ked.core.entities.User user = userService.create(chatId, name);
            addDefaultCategory(ETransaction.INCOME, user.getId());
            addDefaultCategory(ETransaction.EXPENSE, user.getId());
        }
    }

    private void addDefaultCategory(ETransaction eTransaction, Long userId) {
        categoryService.setType(eTransaction.toString(), userId);
        categoryService.setName(DEFAULT_CATEGORY, userId);
        categoryService.setCreatedAt(userId);
    }

    private void sendHelloMessage(long chatId, AbsSender absSender) {
        MessageUtil.sendMessage(chatId, HELLO_MESSAGE, absSender);
    }
}
