package com.ppfss.telegram_dashboard;

import com.ppfss.libs.config.YamlConfigLoader;
import com.ppfss.libs.message.Message;
import com.ppfss.libs.utils.LogUtils;
import com.ppfss.telegram_dashboard.bot.TelegramBot;
import com.ppfss.telegram_dashboard.command.TelegramCommand;
import com.ppfss.telegram_dashboard.config.Config;
import com.ppfss.telegram_dashboard.config.MessageConfig;
import com.ppfss.telegram_dashboard.config.UserConfig;
import org.bukkit.plugin.java.JavaPlugin;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class PPFSS_Telegram extends JavaPlugin {
    private final ExecutorService bot = Executors.newSingleThreadExecutor(r ->new Thread(r, "TelegramBot"));
    private TelegramBot telegramBot;
    private YamlConfigLoader configLoader;
    private TelegramClient telegramClient;


    @Override
    public void onEnable() {
        Message.load(this);
        LogUtils.init(this);
        configLoader = new YamlConfigLoader(this);

        Config.load(configLoader);
        if (Config.getInstance().isDebug()){
            LogUtils.setDEBUG_ENABLED(true);
        }

        MessageConfig.load(configLoader);
        UserConfig.load(configLoader);



        String token = Config.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            LogUtils.error("Установите конфиг бота");
            throw new RuntimeException("You need to set a token");
        }

        telegramClient = new OkHttpTelegramClient(token);

        new TelegramCommand(this, configLoader, telegramClient);
        LogUtils.info("Команды зарегистрированы");

        bot.submit(() -> {
            telegramBot = new TelegramBot(token, this, telegramClient);
        });


    }


    @Override
    public void onDisable() {
        try {
            if (telegramBot != null) {
                telegramBot.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!bot.isShutdown()) {
            bot.shutdown();
            try{
                if (!bot.awaitTermination(5, TimeUnit.SECONDS)){
                    bot.shutdownNow();
                }
            }catch (Exception exception){
                bot.shutdownNow();
                LogUtils.error("TelegramBot shutdown failed", exception);
            }
        }

        configLoader.saveAll();
    }
}
