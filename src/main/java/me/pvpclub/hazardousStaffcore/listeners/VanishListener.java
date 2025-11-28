package me.pvpclub.hazardousStaffcore.listeners;

import me.pvpclub.hazardousStaffcore.HazardousStaffcore;
import org.bukkit.Bukkit;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VanishListener implements Listener {

    private final HazardousStaffcore plugin;

    public VanishListener(HazardousStaffcore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getVanishManager().handleJoin(player);

        if (plugin.getVanishManager().isVanished(player)) {
            event.setJoinMessage(null);
            plugin.getVanishManager().updateVanishState(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (plugin.getVanishManager().isVanished(player)) {
            event.setQuitMessage(null);
        }
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if (plugin.getVanishManager().isVanished(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChestOpen(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getVanishManager().isVanished(player)) return;

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (event.getClickedBlock() == null) return;

        if (plugin.getConfig().getBoolean("vanish.silent-chest-open", true)) {
            if (event.getClickedBlock().getState() instanceof Chest) {
                Chest chest = (Chest) event.getClickedBlock().getState();
                player.openInventory(chest.getInventory());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        if (!(event.getTarget() instanceof Player)) return;

        Player player = (Player) event.getTarget();

        if (plugin.getVanishManager().isVanished(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            if (plugin.getVanishManager().isVanished(damager)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMessage(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase();

        if (message.startsWith("/msg") || message.startsWith("/tell") ||
                message.startsWith("/w") || message.startsWith("/whisper") ||
                message.startsWith("/m")) {

            String[] args = event.getMessage().split(" ");
            if (args.length < 2) return;

            String targetName = args[1];
            Player target = Bukkit.getPlayer(targetName);

            if (target != null && plugin.getVanishManager().isVanished(target)) {
                int vanishLevel = plugin.getVanishManager().getVanishLevel(target);
                if (!plugin.getVanishManager().canSeeVanished(event.getPlayer(), vanishLevel)) {
                    event.getPlayer().sendMessage("§cPlayer not found.");
                    event.setCancelled(true);
                }
            }
        }
    }
}