// PPFSS_Telegram Plugin 
// Авторские права (c) 2025 PPFSS
// Лицензия: MIT

package com.ppfss.telegram_dashboard.bot.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

@SuppressWarnings("unused")
public class TelegramSender implements ConsoleCommandSender {

    private final ConsoleCommandSender wrapped;
    private final StringBuilder log = new StringBuilder();

    public TelegramSender(@NotNull ConsoleCommandSender wrapped) {
        this.wrapped = wrapped;
    }

    public String getLog() {
        return log.toString();
    }

    public String getLogStripped() {
        return log.toString();
    }

    public void clearLog() {
        log.setLength(0);
    }

    @Override
    public void sendMessage(@NotNull String s) {
        log.append(s).append('\n');
        wrapped.sendMessage(s);
    }

    @Override
    public void sendMessage(@NotNull String... strings) {
        for (String s : strings) {
            log.append(s).append('\n');
        }
        wrapped.sendMessage(strings);
    }

    @Override
    public void sendMessage(@Nullable UUID uuid, @NotNull String s) {
        log.append('[').append(uuid == null ? "null" : uuid.toString()).append("] ").append(s).append('\n');
        wrapped.sendMessage(uuid, s);
    }

    @Override
    public void sendMessage(@Nullable UUID uuid, @NotNull String... strings) {
        for (String s : strings) {
            log.append('[').append(uuid == null ? "null" : uuid.toString()).append("] ").append(s).append('\n');
        }
        wrapped.sendMessage(uuid, strings);
    }

    @Override
    public void sendRawMessage(@NotNull String s) {
        log.append("(raw) ").append(s).append('\n');
        wrapped.sendRawMessage(s);
    }

    @Override
    public void sendRawMessage(@Nullable UUID uuid, @NotNull String s) {
        log.append("(raw)[").append(uuid == null ? "null" : uuid.toString()).append("] ").append(s).append('\n');
        wrapped.sendRawMessage(uuid, s);
    }

    @Override
    public @NotNull Server getServer() {
        return wrapped.getServer();
    }

    @Override
    public @NotNull String getName() {
        return wrapped.getName();
    }

    @Override
    public @NotNull Spigot spigot() {
        return wrapped.spigot();
    }

    @Override
    public @NotNull Component name() {
        return wrapped.name();
    }

    @Override
    public boolean isConversing() {
        return wrapped.isConversing();
    }

    @Override
    public void acceptConversationInput(@NotNull String s) {
        wrapped.acceptConversationInput(s);
    }

    @Override
    public boolean beginConversation(@NotNull Conversation conversation) {
        return wrapped.beginConversation(conversation);
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation) {
        wrapped.abandonConversation(conversation);
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation, @NotNull ConversationAbandonedEvent conversationAbandonedEvent) {
        wrapped.abandonConversation(conversation, conversationAbandonedEvent);
    }

    @Override
    public boolean isPermissionSet(@NotNull String s) {
        return wrapped.isPermissionSet(s);
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission permission) {
        return wrapped.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(@NotNull String s) {
        return wrapped.hasPermission(s);
    }

    @Override
    public boolean hasPermission(@NotNull Permission permission) {
        return wrapped.hasPermission(permission);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b) {
        return wrapped.addAttachment(plugin, s, b);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        return wrapped.addAttachment(plugin);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b, int i) {
        return wrapped.addAttachment(plugin, s, b, i);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, int i) {
        return wrapped.addAttachment(plugin, i);
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment permissionAttachment) {
        wrapped.removeAttachment(permissionAttachment);
    }

    @Override
    public void recalculatePermissions() {
        wrapped.recalculatePermissions();
    }

    @Override
    public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return wrapped.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return wrapped.isOp();
    }

    @Override
    public void setOp(boolean b) {
        wrapped.setOp(b);
    }
}