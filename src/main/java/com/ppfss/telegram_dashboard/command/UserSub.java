// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.command;

import com.ppfss.libs.command.SubCommand;
import com.ppfss.libs.message.Message;
import com.ppfss.libs.message.Placeholders;
import com.ppfss.telegram_dashboard.bot.message.TelegramMessage;
import com.ppfss.telegram_dashboard.config.MessageConfig;
import com.ppfss.telegram_dashboard.config.UserConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

public class UserSub extends SubCommand {
    private final Message USAGE = new Message(
            "<yellow>/tg user {add/remove} {UserId} <white>- добавляет/удаляет пользователя",
            "<yellow>/tg user {list} - показывает список пользователей"
    );
    private final String PERMISSION = "telegram.user";
    private final TelegramClient client;

    public UserSub(TelegramClient client) {
        super("user");
        this.client = client;
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String... args) {
        if (args.length == 0) {
            sendUsage(sender, command, label, args);
            return;
        }


        switch (args[0].toLowerCase()) {
            case "add": {
                Long chatId = prepareTransaction(sender, command, label, args);
                if (chatId == null) return;

                TelegramMessage telegramMessage = new TelegramMessage("Добавили нового <a href=\"tg://user?id=%s\">пользователя</a>".formatted(chatId.toString()));

                UserConfig.getInstance().getAllowedUsers().forEach(id ->
                        telegramMessage.sendAsync(id, client)
                );

                UserConfig.getInstance().getAllowedUsers().add(chatId);
                UserConfig.getInstance().save();
                return;
            }
            case "list": {
                List<String> users = UserConfig.getInstance().getAllowedUsers().stream().map(String::valueOf).toList();
                if (users.isEmpty()) users = List.of("Отсутствуют");
                Placeholders placeholders = Placeholders.of().add("user", users);

                MessageConfig.getInstance().getUserList().send(sender, placeholders);
                return;
            }
            case "remove": {
                Long chatId = prepareTransaction(sender, command, label, args);
                if (chatId == null) return;

                TelegramMessage telegramMessage = new TelegramMessage("Удалили <a href=\"tg://user?id=%s\">пользователя</a>".formatted(chatId.toString()));

                UserConfig.getInstance().getAllowedUsers().forEach(id ->
                        telegramMessage.sendAsync(id, client)
                );

                UserConfig.getInstance().getAllowedUsers().remove(chatId);
                UserConfig.getInstance().save();
                return;
            }
            default: {
                sendUsage(sender, command, label, args);
            }
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String... args) {
        if (!sender.hasPermission(PERMISSION)) {
            return null;
        }

        return switch (args.length){
            case 1 -> List.of("add", "remove", "list");

            case 2 -> {
                if (args[0].equalsIgnoreCase("add")) {
                    yield List.of("UserID");
                }
                if  (args[0].equalsIgnoreCase("remove")) {
                    yield UserConfig.getInstance().getAllowedUsers().stream().map(String::valueOf).toList();
                }
                yield  null;
            }

            default -> null;
        };
    }

    private Long prepareTransaction(CommandSender sender, Command command, String label, String... args){
        if (args.length < 2) {
            sendUsage(sender, command, label, args);
            return null;
        }

        long chatId;

        try{
            chatId = Long.parseLong(args[1]);
        }catch (NumberFormatException exception){
            MessageConfig.getInstance().getNotNumber().send(sender);
            return null;
        }

        return chatId;
    }

    @Override
    public String getPermission(CommandSender sender, Command command, String label, String... args) {
        return PERMISSION;
    }


    @Override
    public void sendUsage(CommandSender sender, Command command, String label, String... args) {
        USAGE.send(sender);
    }
}
