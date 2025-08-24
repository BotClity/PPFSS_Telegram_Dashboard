// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.bot.command;

import com.ppfss.libs.utils.LogUtils;
import com.ppfss.telegram_dashboard.config.MessageConfig;
import com.ppfss.telegram_dashboard.config.UserConfig;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class StartCommand extends TelegramCommand {
    public StartCommand() {
        super("start");
    }

    @Override
    public void execute(Update update) {
        MessageConfig.getInstance().getTelegramSettings().getHelpCommand()
                .sendAsync(update.getMessage().getChatId(), getClient())
                .exceptionally(LogUtils::logExceptionally);
    }

    @Override
    public List<Long> getAllowedUsers() {
        return UserConfig.getInstance().getAllowedUsers();
    }

    @Override
    public void noPermission(Update update) {
        MessageConfig.getInstance().getTelegramSettings().getNoPermission()
                .sendAsync(update.getMessage().getChatId(), getClient())
                .exceptionally(LogUtils::logExceptionally);
    }

    @Override
    public @NotNull String getDescription() {
        return "Список доступных команд";
    }
}
