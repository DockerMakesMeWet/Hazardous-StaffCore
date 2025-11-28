package me.pvpclub.hazardousStaffcore.listeners;

import me.pvpclub.hazardousStaffcore.HazardousStaffcore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class FreezeListener implements Listener {

    private final HazardousStaffcore plugin;

    public FreezeListener(HazardousStaffcore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (plugin.getFreezeManager().isFrozen(player)) {
            if (event.getFrom().getBlockX() != event.getTo().getBlockX() ||
                    event.getFrom().getBlockY() != event.getTo().getBlockY() ||
                    event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getFreezeManager().handleQuit(event.getPlayer());
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (plugin.getFreezeManager().isFrozen(player)) {
            String command = event.getMessage().toLowerCase();

            if (!command.startsWith("/helpop") && !command.startsWith("/report")) {
                event.setCancelled(true);
                player.sendMessage("§c§l[!] You cannot use commands while frozen!");
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if (plugin.getFreezeManager().isFrozen(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (plugin.getFreezeManager().isFrozen(player)) {
            event.setCancelled(true);
            player.sendMessage("§c§l[!] You cannot chat while frozen!");
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (plugin.getFreezeManager().isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (plugin.getFreezeManager().isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();

        if (plugin.getFreezeManager().isFrozen(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (plugin.getFreezeManager().isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}