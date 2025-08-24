// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.model;

import com.google.gson.annotations.SerializedName;
import com.ppfss.telegram_dashboard.bot.message.TelegramMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TelegramSettings {
    @SerializedName("info")
    TelegramMessage infoCommand = new TelegramMessage(
            "<b>📊 Информация сервера</b>",
            " ",
            "👥 Онлайн: <b><online></b>",
            "⚡ TPS: <b><tps></b>",
            "⏱ MSPT: <b><mspt></b>",
            "📡 Пинг: <b><ping></b>",
            "🔥 ЦП: <b><cpu></b>%",
            "💾 ОЗУ: <b><ram></b>%",
            "📂 Хранилище: <b><disk></b>%"
    );


    @SerializedName("help")
    TelegramMessage helpCommand = new TelegramMessage(
            "Список команд:",
            " ",
            "/cmd — отправка команд на сервер",
            "/info — информация о сервере",
            "/help — показать это сообщение"
    );


    transient TelegramMessage noPermission = new TelegramMessage(
            "❌ У вас нет прав для выполнения этой команды.",
            " ",
            "©<a href=\"https://t.me/unpronounceables\">PPFSS</a>"
    );
}
