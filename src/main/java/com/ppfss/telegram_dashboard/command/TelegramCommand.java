// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.command;

import com.ppfss.libs.command.AbstractCommand;
import com.ppfss.libs.config.YamlConfigLoader;
import org.bukkit.plugin.Plugin;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

public class TelegramCommand extends AbstractCommand {
    public TelegramCommand(Plugin plugin, YamlConfigLoader loader, TelegramClient telegramClient) {
        super("telegram", List.of("tg", "телеграм"), plugin);
        registerSubCommand(new ReloadSub(loader));
        registerSubCommand(new UserSub(telegramClient));
    }


}
