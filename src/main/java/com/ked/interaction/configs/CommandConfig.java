package com.ked.interaction.configs;

import com.ked.interaction.commands.AddTransactionCommand;
import com.ked.interaction.commands.ConfigureCommand;
import com.ked.interaction.commands.InfoCommand;
import com.ked.interaction.commands.SendBotMessageCommand;
import com.ked.interaction.commands.StartCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;

@Configuration
public class CommandConfig {
    @Value("${telegram.bot.name}")
    private String name;

    @Bean
    public CommandRegistry commandRegistry(
            @Autowired StartCommand startCommand,
            @Autowired InfoCommand infoCommand,
            @Autowired SendBotMessageCommand sendBotMessageCommand,

            @Autowired AddTransactionCommand addTransactionCommand,
            @Autowired ConfigureCommand configureCommand
            ) {
        CommandRegistry commandRegistry = new CommandRegistry(true, () -> name);

        commandRegistry.register(startCommand);
        commandRegistry.register(infoCommand);
        commandRegistry.register(sendBotMessageCommand);

        commandRegistry.register(addTransactionCommand);
        commandRegistry.register(configureCommand);

        return commandRegistry;
    }
}
