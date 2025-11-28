package me.pvpclub.hazardousStaffcore.managers;

import me.pvpclub.hazardousStaffcore.HazardousStaffcore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatManager {

    private final HazardousStaffcore plugin;
    private FileConfiguration chatsConfig;
    private Map<UUID, String> activeChats;
    private Map<String, ChatChannel> channels;

    public ChatManager(HazardousStaffcore plugin) {
        this.plugin = plugin;
        this.activeChats = new HashMap<>();
        this.channels = new HashMap<>();
        loadChatsConfig();
    }

    private void loadChatsConfig() {
        File chatsFile = new File(plugin.getDataFolder(), "chats.yml");
        chatsConfig = YamlConfiguration.loadConfiguration(chatsFile);

        for (String key : chatsConfig.getConfigurationSection("chats").getKeys(false)) {
            String name = chatsConfig.getString("chats." + key + ".name");
            String permission = chatsConfig.getString("chats." + key + ".permission");
            String format = chatsConfig.getString("chats." + key + ".format");
            String prefix = chatsConfig.getString("chats." + key + ".prefix");

            channels.put(key.toLowerCase(), new ChatChannel(name, permission, format, prefix));
        }
    }

    public ChatChannel getChannel(String channelKey) {
        return channels.get(channelKey.toLowerCase());
    }

    public void toggleChat(Player player, String channelKey) {
        UUID uuid = player.getUniqueId();
        if (activeChats.containsKey(uuid) && activeChats.get(uuid).equals(channelKey)) {
            activeChats.remove(uuid);
            player.sendMessage("§cYou have left " + channels.get(channelKey).getName() + ".");
        } else {
            activeChats.put(uuid, channelKey);
            player.sendMessage("§aYou are now chatting in " + channels.get(channelKey).getName() + ".");
        }
    }

    public String getActiveChat(UUID uuid) {
        return activeChats.get(uuid);
    }

    public void sendMessage(Player sender, String channelKey, String message) {
        ChatChannel channel = channels.get(channelKey);
        if (channel == null) return;

        String formattedMessage = channel.getFormat()
                .replace("{prefix}", channel.getPrefix())
                .replace("{player}", sender.getName())
                .replace("{message}", message);

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.hasPermission(channel.getPermission())) {
                online.sendMessage(formattedMessage);
            }
        }

        plugin.getDatabaseManager().logStaffChat(channelKey, sender.getUniqueId().toString(), message);
    }

    public static class ChatChannel {
        private final String name;
        private final String permission;
        private final String format;
        private final String prefix;

        public ChatChannel(String name, String permission, String format, String prefix) {
            this.name = name;
            this.permission = permission;
            this.format = format;
            this.prefix = prefix;
        }

        public String getName() {
            return name;
        }

        public String getPermission() {
            return permission;
        }

        public String getFormat() {
            return format;
        }

        public String getPrefix() {
            return prefix;
        }
    }
}