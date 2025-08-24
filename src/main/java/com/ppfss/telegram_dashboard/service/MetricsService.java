// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class MetricsService {
    private final Plugin plugin;

    public MetricsService(Plugin plugin) {
        this.plugin = plugin;
    }

    public int getOnlinePlayers(){
        return Bukkit.getOnlinePlayers().size();
    }

    public CompletableFuture<Integer> getAverageIp(){
        CompletableFuture<Integer> future = new CompletableFuture<>();

        new BukkitRunnable(){
            @Override
            public void run() {
                Collection<? extends Player> players = Bukkit.getOnlinePlayers();
                if  (players.isEmpty()) {
                    future.complete(0);
                    return;
                }

                double average = players.stream()
                        .mapToInt(Player::getPing)
                        .average()
                        .orElse(0);

                future.complete((int) average);
            }
        }.runTask(plugin);

        return future;
    }
}
