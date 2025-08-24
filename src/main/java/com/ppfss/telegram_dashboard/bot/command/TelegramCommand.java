// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.bot.command;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Getter
@Setter
public abstract class TelegramCommand {
    private final String commandName;
    private TelegramClient client;

    public TelegramCommand(String commandName) {
        this.commandName = commandName.startsWith("/") ? commandName : "/" + commandName;
    }

    public abstract void execute(Update update);

    /**
     * return null for everybody
     * return empty list for no one
     */
    public List<Long> getAllowedChats() {
        return null;
    }

    /**
     * return null for everybody
     * return empty list for no one
     */
    public List<Long> getAllowedUsers() {
        return null;
    }

    public boolean display(){
        return true;
    }

    @NotNull
    public String getDescription() {
        return "";
    }

    public BotCommand getBotCommand() {
        return new BotCommand(commandName, getDescription());
    }

    public void noPermission(Update update) {

    }
}
