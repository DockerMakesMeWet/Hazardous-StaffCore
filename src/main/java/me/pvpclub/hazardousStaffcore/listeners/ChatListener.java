package me.pvpclub.hazardousStaffcore.listeners;

import me.pvpclub.hazardousStaffcore.HazardousStaffcore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChatListener implements Listener {

    private final HazardousStaffcore plugin;

    public ChatListener(HazardousStaffcore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String activeChannel = plugin.getChatManager().getActiveChat(player.getUniqueId());

        if (activeChannel != null) {
            event.setCancelled(true);
            plugin.getChatManager().sendMessage(player, activeChannel, event.getMessage());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getChatManager().removeFromChat(event.getPlayer().getUniqueId());
    }
}

