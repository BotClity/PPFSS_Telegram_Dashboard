// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.bot.service;

import com.ppfss.libs.utils.LogUtils;
import com.ppfss.telegram_dashboard.bot.command.TelegramCommand;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CommandService extends UpdateProcessor {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final Map<String, TelegramCommand> commands = new ConcurrentHashMap<>();


    private void processCommand(Update update) {

        executor.submit(() -> {
            String commandName = update.getMessage().getText()
                    .trim()
                    .split(" ")[0]
                    .toLowerCase();

            TelegramCommand command = commands.get(commandName);
            if (command == null) return;

            Long chatId = update.getMessage().getChatId();
            Long userId = update.getMessage().getFrom().getId();

            if (command.getAllowedChats() != null && !command.getAllowedChats().contains(chatId)) {
                command.noPermission(update);
                return;
            }
            if (command.getAllowedUsers() != null && !command.getAllowedUsers().contains(userId)) {
                command.noPermission(update);
                return;
            }


            command.execute(update);
        });
    }

    public void registerCommand(TelegramCommand command) {
        command.setClient(telegramClient);
        commands.put(command.getCommandName(), command);
    }

    @Override
    public void process(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;
        if (!update.getMessage().isCommand()) return;

        processCommand(update);
    }

    public List<BotCommand> getCommands() {
        return commands.values().stream().map(TelegramCommand::getBotCommand).collect(Collectors.toList());
    }

    @Override
    public void close() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (Exception e) {
            executor.shutdownNow();
            LogUtils.error("Failed to close command service");
        }
    }
}
