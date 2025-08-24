// Protection Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.bot;

import com.ppfss.libs.utils.LogUtils;
import com.ppfss.telegram_dashboard.bot.command.*;
import com.ppfss.telegram_dashboard.bot.message.TelegramMessage;
import com.ppfss.telegram_dashboard.bot.service.CommandService;
import com.ppfss.telegram_dashboard.bot.service.UpdateProcessor;
import com.ppfss.telegram_dashboard.bot.service.UpdateService;
import com.ppfss.telegram_dashboard.service.MetricsService;
import com.ppfss.telegram_dashboard.service.PerformanceService;
import org.bukkit.plugin.Plugin;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

public class TelegramBot implements AutoCloseable {
    private final TelegramBotsLongPollingApplication application = new TelegramBotsLongPollingApplication();
    private final TelegramClient client;
    private final UpdateService updateService;
    private final MetricsService metricsService;
    private final PerformanceService performanceService;
    private final Plugin plugin;

    public TelegramBot(String token, Plugin plugin, TelegramClient client) {
        this.plugin = plugin;

        this.client = client;

        updateService = new UpdateService(client);
        metricsService = new MetricsService(plugin);
        performanceService = new PerformanceService();

        LongPollingUpdateConsumer updateConsumer = updateService::onUpdatesReceived;



        try {
            registerProcessors(updateService);

            application.registerBot(token, updateConsumer);
        } catch (TelegramApiException exception) {
            LogUtils.error("TelegramBot register error", exception);
            throw new RuntimeException("TelegramBot register error", exception);
        }
    }

    @Override
    public void close() throws Exception {
        updateService.close();
        application.close();
    }

    private void registerProcessors(UpdateService service) throws TelegramApiException {
        CommandService commandService = new CommandService();

        service.registerProcessor(commandService);

        commandService.registerCommand(new InfoCommand(metricsService, performanceService));
        commandService.registerCommand(new CmdCommand(plugin));
        commandService.registerCommand(new HelpCommand());
        commandService.registerCommand(new StartCommand());



        registerDisplay(commandService);
    }

    private void registerDisplay(CommandService commandService) throws TelegramApiException {
        List<BotCommand> commands = commandService.getCommands();
        SetMyCommands setMyCommands = new SetMyCommands(commands);

        client.execute(setMyCommands);
    }
}
