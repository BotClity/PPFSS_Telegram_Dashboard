// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.config;

import com.google.gson.annotations.SerializedName;
import com.ppfss.libs.config.YamlConfig;
import com.ppfss.libs.config.YamlConfigLoader;
import com.ppfss.libs.message.Message;
import com.ppfss.telegram_dashboard.model.TelegramSettings;
import lombok.Getter;

@Getter
public class MessageConfig extends YamlConfig {
    private static MessageConfig instance;

    @SerializedName("telegram")
    TelegramSettings telegramSettings = new TelegramSettings();

    @SerializedName("no-permission")
    Message noPermission = new Message("<red>Недостаточно прав!");
    @SerializedName("user-added")
    Message userAdded = new Message("<green>Пользователь добавлен");
    @SerializedName("user-removed")
    Message userRemoved = new Message("<green>Пользователь удален");
    @SerializedName("user-list")
    Message userList = new Message(
            "<yellow>Пользователи:",
            "<yellow><user>"
    );

    @SerializedName("not-number")
    Message notNumber = new Message("<red>Вы ввели не число");


    public static void load(YamlConfigLoader loader) {
        instance = loader.loadConfig("messages", MessageConfig.class);
    }

    public static MessageConfig getInstance() {
        if (instance == null) throw new IllegalStateException("MessageConfig instance has not been initialized");
        return instance;
    }
}
