package me.pvpclub.hazardousStaffcore.listeners;

import me.pvpclub.hazardousStaffcore.HazardousStaffcore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StaffModeListener implements Listener {

    private final HazardousStaffcore plugin;

    public StaffModeListener(HazardousStaffcore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (plugin.getStaffModeManager().isInStaffMode(player)) {
            plugin.getStaffModeManager().disableStaffMode(player);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if (plugin.getStaffModeManager().isInStaffMode(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if (plugin.getStaffModeManager().isInStaffMode(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        if (plugin.getStaffModeManager().isInStaffMode(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();

        if (plugin.getStaffModeManager().isInStaffMode(player)) {
            if (event.getClickedInventory() == player.getInventory()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getStaffModeManager().isInStaffMode(player)) return;

        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta()) return;

        String displayName = item.getItemMeta().getDisplayName();

        if (displayName.contains("Random Teleport") && event.getAction() == Action.RIGHT_CLICK_AIR) {
            event.setCancelled(true);

            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            players.remove(player);

            if (players.isEmpty()) {
                player.sendMessage("§cNo players to teleport to!");
                return;
            }

            Player target = players.get(new Random().nextInt(players.size()));
            player.teleport(target);
            player.sendMessage("§aTeleported to " + target.getName() + "!");
        }

        if (displayName.contains("Vanish Toggle")) {
            event.setCancelled(true);
            plugin.getVanishManager().toggleVanish(player);
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getStaffModeManager().isInStaffMode(player)) return;
        if (!(event.getRightClicked() instanceof Player)) return;

        Player target = (Player) event.getRightClicked();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || !item.hasItemMeta()) return;

        String displayName = item.getItemMeta().getDisplayName();

        if (displayName.contains("Freeze Player")) {
            event.setCancelled(true);

            if (plugin.getFreezeManager().isFrozen(target)) {
                plugin.getFreezeManager().unfreezePlayer(target, player);
            } else {
                plugin.getFreezeManager().freezePlayer(target, player);
            }
        }

        if (displayName.contains("Inspect Inventory")) {
            event.setCancelled(true);
            player.openInventory(target.getInventory());
            player.sendMessage("§aOpened " + target.getName() + "'s inventory!");
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        Player target = (Player) event.getEntity();

        if (!plugin.getStaffModeManager().isInStaffMode(damager)) return;

        ItemStack item = damager.getInventory().getItemInMainHand();

        if (item == null || !item.hasItemMeta()) return;

        String displayName = item.getItemMeta().getDisplayName();

        if (displayName.contains("Freeze Player")) {
            event.setCancelled(true);

            if (plugin.getFreezeManager().isFrozen(target)) {
                plugin.getFreezeManager().unfreezePlayer(target, damager);
            } else {
                plugin.getFreezeManager().freezePlayer(target, damager);
            }
        }
    }
}