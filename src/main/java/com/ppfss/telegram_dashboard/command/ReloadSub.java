// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.command;

import com.ppfss.libs.command.SubCommand;
import com.ppfss.libs.config.YamlConfigLoader;
import com.ppfss.libs.message.Message;
import com.ppfss.telegram_dashboard.config.Config;
import com.ppfss.telegram_dashboard.config.MessageConfig;
import com.ppfss.telegram_dashboard.config.UserConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadSub extends SubCommand {
    private final YamlConfigLoader loader;
    private final Message userConfigReloaded = new Message("<green>Конфиг пользователей перезагружен");
    private final Message configReloaded = new Message("<green>Главный конфиг перезагружен");
    private final Message messageConfigReloaded = new Message("<green>Конфиг сообщений перезагружен");

    public ReloadSub(YamlConfigLoader loader) {
        super("reload");
        this.loader = loader;
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String... args) {
        UserConfig.load(loader);
        userConfigReloaded.send(sender);
        Config.load(loader);
        configReloaded.send(sender);
        MessageConfig.load(loader);
        messageConfigReloaded.send(sender);
    }

    @Override
    public void noPermission(CommandSender sender, Command command, String label, String... args) {
        MessageConfig.getInstance().getNoPermission().send(sender);
    }

    @Override
    public String getPermission(CommandSender sender, Command command, String label, String... args) {
        return "telegram.reload";
    }

    @Override
    public void sendUsage(CommandSender sender, Command command, String label, String... args) {
        sender.sendMessage("/telegram reload");
    }
}
