// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.bot.command;

import com.ppfss.libs.utils.LogUtils;
import com.ppfss.telegram_dashboard.bot.message.TelegramMessage;
import com.ppfss.telegram_dashboard.bot.utils.TelegramLog4jHandler;
import com.ppfss.telegram_dashboard.bot.utils.TelegramSender;
import com.ppfss.telegram_dashboard.config.Config;
import com.ppfss.telegram_dashboard.config.MessageConfig;
import com.ppfss.telegram_dashboard.config.UserConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CmdCommand extends TelegramCommand {
    private final Plugin plugin;
    private final TelegramSender telegramSender;
    private final ExecutorService cmdExecutor = Executors.newSingleThreadExecutor();
    private final TelegramMessage USAGE = new TelegramMessage("/cmd {команда...}");
    private final TelegramMessage SUCCESS = new TelegramMessage("Команда успешно выполнена, но ответной информации не поступило.");
    private final TelegramLog4jHandler log4jHandler;

    public CmdCommand(Plugin plugin) {
        super("cmd");
        this.plugin = plugin;
        this.telegramSender = new TelegramSender(Bukkit.getConsoleSender());
        this.log4jHandler = new TelegramLog4jHandler();
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String cmd = update.getMessage().getText();

        String command;
        if (cmd.startsWith("/cmd ")) {
            command = cmd.substring(5);
        }else{
            USAGE.send(chatId, getClient());
            return;
        }

        if (command.trim().isEmpty()) {
            USAGE.send(chatId, getClient());
            return;
        }else if (command.trim().startsWith("/")) {
            command = command.substring(1);
        }

        executeCmd(command).thenAccept(str -> {
            if (str == null || str.isEmpty()) {
                SUCCESS.sendAsync(chatId, getClient());
                return;
            }

            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(str)
                    .parseMode("HTML")
                    .build();
            try {
                getClient().executeAsync(message);
            } catch (TelegramApiException e) {
                LogUtils.error("Failed to send telegram message {}", message, e);
                throw new RuntimeException("Failed to send telegram message " + e.getMessage());
            }
        });
    }

    private CompletableFuture<String> executeCmd(String cmd) {
        CompletableFuture<String> future = new CompletableFuture<>();
        cmdExecutor.submit(() -> {

            new BukkitRunnable() {
                @Override
                public void run() {
                    boolean dirtyCmd = Config.getInstance().isDirtyCmd();
                    if (dirtyCmd){
                        log4jHandler.enable();
                    }

                    try {
                        Bukkit.dispatchCommand(telegramSender, cmd);
                        String logs = telegramSender.getLog();
                        if (dirtyCmd){
                            logs = logs + "\n" + String.join("\n", log4jHandler.getLogs());
                        }
                        future.complete(logs);
                    } catch (Exception e) {
                        future.completeExceptionally(e);
                    } finally {
                        telegramSender.clearLog();
                        if (dirtyCmd) {
                            log4jHandler.disable();
                            log4jHandler.clear();
                        }
                    }
                }
            }.runTask(plugin);
        });

        return future;
    }

    @Override
    public List<Long> getAllowedUsers() {
        return UserConfig.getInstance().getAllowedUsers();
    }

    @Override
    public void noPermission(Update update) {
        Long chatId = update.getMessage().getChatId();
        MessageConfig.getInstance().getTelegramSettings().getNoPermission().send(chatId, getClient());
    }

    @Override
    public @NotNull String getDescription() {
        return "Выполняет удаленно команду на сервере";
    }


}
