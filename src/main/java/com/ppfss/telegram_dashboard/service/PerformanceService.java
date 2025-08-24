// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.service;

import com.sun.management.OperatingSystemMXBean;
import org.bukkit.Bukkit;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.concurrent.CompletableFuture;

public class PerformanceService {
    private final Runtime runtime;
    private final OperatingSystemMXBean OSBean;

    public PerformanceService() {
        this.runtime = Runtime.getRuntime();
        this.OSBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    public int getRamUsage(){
        double used = runtime.totalMemory() -  runtime.freeMemory();
        double usage = used/runtime.maxMemory() * 100;
        return (int) usage;
    }

    public int getCpuUsage(){
        double usage = OSBean.getCpuLoad() * 100;
        if (usage < 0) return 0;

        return (int) usage;
    }

    public int getTPSUsage(){
        double tps = Bukkit.getServer().getTPS()[0];
        return (int) tps;
    }

    public int getMSPTUsage(){
        double mspt = Bukkit.getServer().getAverageTickTime();
        return (int) mspt;
    }

    public CompletableFuture<Integer> getDiskUsageAsync() {
        return CompletableFuture.supplyAsync(() -> {
            File file = new File(".");
            long total = file.getTotalSpace();
            long free = file.getFreeSpace();
            long used = total - free;
            return (int) ((double) used / total * 100);
        });
    }
}
