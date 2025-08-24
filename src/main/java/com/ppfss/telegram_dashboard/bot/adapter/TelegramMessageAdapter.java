// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.bot.adapter;

import com.google.gson.*;
import com.ppfss.telegram_dashboard.bot.message.TelegramMessage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TelegramMessageAdapter implements JsonSerializer<TelegramMessage>, JsonDeserializer<TelegramMessage> {
    @Override
    public TelegramMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        List<String> lines = new ArrayList<>();

        if (jsonElement.isJsonArray()) {
            for (JsonElement line : jsonElement.getAsJsonArray()) {
                lines.add(line.getAsString());
            }
        }else if(jsonElement.isJsonPrimitive()){
            lines.add(jsonElement.getAsString());
        }else {
            throw new JsonParseException("Failed to parse json: " + jsonElement);
        }

        return new TelegramMessage(lines);
    }

    @Override
    public JsonElement serialize(TelegramMessage telegramMessage, Type type, JsonSerializationContext jsonSerializationContext) {
        List<String> lines = telegramMessage.getRawMessage();
        if (lines.isEmpty()) throw new JsonParseException("Raw message is empty");

        if (lines.size() == 1) {
            return new JsonPrimitive(lines.get(0));
        }

        JsonArray linesArray = new JsonArray();
        for (String line : lines) {
            linesArray.add(line);
        }

        return linesArray;
    }
}
