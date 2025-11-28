package me.pvpclub.hazardousStaffcore.listeners;

import me.pvpclub.hazardousStaffcore.HazardousStaffcore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandLogListener implements Listener {

    private final HazardousStaffcore plugin;

    public CommandLogListener(HazardousStaffcore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();

        if (plugin.getConfig().getBoolean("logging.log-all-commands", true)) {
            plugin.getDatabaseManager().logCommand(player.getUniqueId().toString(), command);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();

        if (title.contains("Command Logs")) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

            String displayName = event.getCurrentItem().getItemMeta().getDisplayName();

            if (displayName.contains("Next Page")) {
                String[] parts = title.split(" ");
                String targetName = parts[3];
                int currentPage = plugin.getLogManager().getCurrentPage(player.getUniqueId());
                plugin.getLogManager().openCommandLogs(player, targetName, currentPage + 1);
            } else if (displayName.contains("Previous Page")) {
                String[] parts = title.split(" ");
                String targetName = parts[3];
                int currentPage = plugin.getLogManager().getCurrentPage(player.getUniqueId());
                plugin.getLogManager().openCommandLogs(player, targetName, currentPage - 1);
            } else if (displayName.contains("Close")) {
                player.closeInventory();
            }
        } else if (title.contains("Creative Logs")) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

            String displayName = event.getCurrentItem().getItemMeta().getDisplayName();

            if (displayName.contains("Next Page")) {
                String[] parts = title.split(" ");
                String targetName = parts[3];
                int currentPage = plugin.getLogManager().getCurrentPage(player.getUniqueId());
                plugin.getLogManager().openCreativeLogs(player, targetName, currentPage + 1);
            } else if (displayName.contains("Previous Page")) {
                String[] parts = title.split(" ");
                String targetName = parts[3];
                int currentPage = plugin.getLogManager().getCurrentPage(player.getUniqueId());
                plugin.getLogManager().openCreativeLogs(player, targetName, currentPage - 1);
            } else if (displayName.contains("Close")) {
                player.closeInventory();
            }
        }
    }
}