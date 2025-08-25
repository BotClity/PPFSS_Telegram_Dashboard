// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.config;

import com.google.gson.annotations.SerializedName;
import com.ppfss.libs.config.YamlConfig;
import com.ppfss.libs.config.YamlConfigLoader;
import lombok.Getter;

@Getter
public class Config extends YamlConfig {
    private static Config instance;

    boolean debug = false;
    String token = "";
    @SerializedName("dirty-telegram-cmd")
    boolean dirtyCmd = false;

    public static void load(YamlConfigLoader loader) {
        instance = loader.loadConfig("config", Config.class);
    }

    public static Config getInstance() {
        if (instance == null) throw new  RuntimeException("Config has not been initialized yet");
        return instance;
    }
}
