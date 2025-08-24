// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.bot.command;

import com.ppfss.libs.message.Placeholders;
import com.ppfss.libs.utils.LogUtils;
import com.ppfss.telegram_dashboard.config.MessageConfig;
import com.ppfss.telegram_dashboard.config.UserConfig;
import com.ppfss.telegram_dashboard.service.MetricsService;
import com.ppfss.telegram_dashboard.service.PerformanceService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class InfoCommand extends TelegramCommand {
    private final MetricsService metricsService;
    private final PerformanceService performanceService;

    public InfoCommand(MetricsService metricsService, PerformanceService performanceService) {
        super("info");
        this.metricsService = metricsService;
        this.performanceService = performanceService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();

        getPlaceholders()
                .thenAccept(placeholders -> MessageConfig.getInstance().getTelegramSettings().getInfoCommand().sendAsync(chatId, getClient(), placeholders))
                .thenRun(() -> LogUtils.debug("Stat sent to chatID {}", chatId))
                .exceptionally(LogUtils::logExceptionally);
    }

    private CompletableFuture<Placeholders> getPlaceholders() {
        CompletableFuture<Integer> diskUsage = performanceService.getDiskUsageAsync();
        CompletableFuture<Integer> ping = metricsService.getAverageIp();

        Placeholders placeholders = new Placeholders();

        placeholders
                .add("cpu", performanceService.getCpuUsage())
                .add("ram", performanceService.getRamUsage())
                .add("tps", performanceService.getTPSUsage())
                .add("mspt", performanceService.getMSPTUsage())
                .add("online", metricsService.getOnlinePlayers());

        return CompletableFuture.allOf(diskUsage, ping)
                .thenApply(v -> {
                    try {
                        placeholders
                                .add("disk", diskUsage.get())
                                .add("ping", ping.get());
                    } catch (Exception e) {
                        LogUtils.error("Failed to get disk usage and ping average", e);
                    }
                    return placeholders;
                });
    }

    @Override
    public void noPermission(Update update) {
        Long chatId = update.getMessage().getChatId();
        MessageConfig.getInstance().getTelegramSettings().getNoPermission().sendAsync(chatId, getClient()).exceptionally(LogUtils::logExceptionally);
    }

    @Override
    public List<Long> getAllowedUsers() {
        return UserConfig.getInstance().getAllowedUsers();
    }

    @Override
    public @NotNull String getDescription() {
        return "Возвращает статистику сервера";
    }
}
