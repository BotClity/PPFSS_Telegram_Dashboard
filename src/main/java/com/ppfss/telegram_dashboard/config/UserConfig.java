// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.config;

import com.ppfss.libs.config.YamlConfig;
import com.ppfss.libs.config.YamlConfigLoader;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserConfig extends YamlConfig {
    private static UserConfig instance;

    private List<Long> allowedUsers = new ArrayList<>();

    public static void load(YamlConfigLoader loader) {
        instance = loader.loadConfig("users", UserConfig.class);
    }

    public static UserConfig getInstance() {
        if (instance == null) throw new RuntimeException("UserConfig is not initialized");
        return instance;
    }
}
