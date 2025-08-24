// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.bot.message;

import com.ppfss.libs.message.Placeholders;
import com.ppfss.libs.utils.LogUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("unused")
public class TelegramMessage {
    @Getter
    private final List<String> rawMessage = new CopyOnWriteArrayList<>();

    private final static String HTML = "HTML";

    public TelegramMessage(String... strings){
        Collections.addAll(this.rawMessage, strings);
    }

    public TelegramMessage(List<String> strings) {
        this.rawMessage.addAll(strings);
    }

    public void addText(String... strings) {
        Collections.addAll(this.rawMessage, strings);
    }

    public void addText(List<String> strings) {
        this.rawMessage.addAll(strings);
    }

    public void addText(TelegramMessage telegramMessage) {
        this.rawMessage.addAll(telegramMessage.rawMessage);
    }

    public CompletableFuture<Message> sendAsync(@NotNull Long chatId, @NotNull TelegramClient client) {
        String msg = parse(rawMessage);

        return sendAsync(msg, chatId, client);
    }

    private CompletableFuture<Message> sendAsync(String msg, Long chatId, @NotNull TelegramClient client) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(msg)
                .parseMode(HTML)
                .disableWebPagePreview(true)
                .build();
        try {
            return client.executeAsync(sendMessage).exceptionally(LogUtils::logExceptionally);
        } catch (TelegramApiException e) {
            LogUtils.error("Error while sending telegram message", e);
            return CompletableFuture.completedFuture(null);
        }
    }

    private Message send(String msg, Long chatId, @NotNull TelegramClient client) {
        LogUtils.info(msg);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(msg)
                .parseMode(HTML)
                .disableWebPagePreview(true)
                .build();
        try {
            return client.execute(sendMessage);
        }catch (TelegramApiException e) {
            LogUtils.error("Error while sending telegram message", e);
            return null;
        }
    }

    public CompletableFuture<Message> sendAsync(@NotNull Long chatId, TelegramClient client, Placeholders placeholders) {
        if (placeholders == null) return sendAsync(chatId, client);
        String msg = parse(placeholders.apply(rawMessage));

        return sendAsync(msg, chatId, client);
    }

    public Message send(@NotNull Long chatId, TelegramClient client, Placeholders placeholders) {
        if (placeholders == null) return send(chatId, client);
        String msg = parse(placeholders.apply(rawMessage));

        return send(msg, chatId, client);
    }


    public Message send(@NotNull Long chatId, @NotNull TelegramClient client) {
        String msg = parse(rawMessage);

        return send(msg, chatId, client);
    }

    private String parse(List<String> strings) {
        return String.join("\n", strings);
    }
}
