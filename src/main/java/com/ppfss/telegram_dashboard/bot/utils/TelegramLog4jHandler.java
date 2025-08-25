// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.bot.utils;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import java.util.ArrayList;
import java.util.List;

public class TelegramLog4jHandler {
    private static final Logger ROOT_LOGGER = (Logger) LogManager.getRootLogger();

    @Getter
    private final List<String> logs = new ArrayList<>();
    private final Appender appender;

    private boolean enabled = false;

    public TelegramLog4jHandler() {
        this.appender = new AbstractAppender("TelegramAppender", null, null, true, null) {
            @Override
            public void append(LogEvent event) {
                String msg = event.getMessage().getFormattedMessage();
                synchronized (logs) {
                    logs.add(msg);
                }
            }
        };
    }

    public void enable() {
        if (enabled) return;
        appender.start();
        ROOT_LOGGER.addAppender(appender);
        enabled = true;
    }

    public void disable() {
        if (!enabled) return;
        ROOT_LOGGER.removeAppender(appender);
        appender.stop();
        enabled = false;
    }

    public void clear() {
        synchronized (logs) {
            logs.clear();
        }
    }
}