// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.bot.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

public class UpdateService implements  AutoCloseable {
    private final List<UpdateProcessor> processors = new ArrayList<>();
    private final TelegramClient telegramClient;

    public UpdateService(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    public void onUpdatesReceived(List<Update> updates) {
        updates.forEach(this::onUpdateReceived);
    }

    public void registerProcessor(UpdateProcessor processor) {
        processor.setTelegramClient(telegramClient);
        processors.add(processor);
    }


    public void onUpdateReceived(Update update) {
        processors.forEach(processor -> processor.process(update));
    }



    @Override
    public void close() throws Exception {
        for (AutoCloseable processor : processors) {
            processor.close();
        }
    }
}
