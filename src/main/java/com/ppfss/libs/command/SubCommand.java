// PPFSS_Quests Plugin
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.libs.command;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public abstract class SubCommand {
    private final String name;

    public SubCommand(String name) {
        this.name = name;
    }

    public void execute(CommandSender sender, Command command, String label, String... args) {
    }

    public List<String> complete(CommandSender sender, String... args) {
        return Collections.emptyList();
    }

    public void noPermission(CommandSender sender, Command command, String label, String... args) {
        sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
    }

    public String getPermission(CommandSender sender, Command command, String label, String... args) {
        return null;
    }

    public abstract void sendUsage(CommandSender sender, Command command, String label, String... args);
}
