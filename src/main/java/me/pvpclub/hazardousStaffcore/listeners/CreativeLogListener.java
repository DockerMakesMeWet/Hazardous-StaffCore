package me.pvpclub.hazardousStaffcore.listeners;

import me.pvpclub.hazardousStaffcore.HazardousStaffcore;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.inventory.ItemStack;

public class CreativeLogListener implements Listener {

    private final HazardousStaffcore plugin;

    public CreativeLogListener(HazardousStaffcore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        GameMode newMode = event.getNewGameMode();

        if (plugin.getConfig().getBoolean("logging.log-gamemode-changes", true)) {
            plugin.getDatabaseManager().logCommand(player.getUniqueId().toString(),
                    "gamemode " + newMode.name().toLowerCase());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCreativeInventory(InventoryCreativeEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();

        if (player.getGameMode() != GameMode.CREATIVE) return;

        if (!plugin.getConfig().getBoolean("logging.log-creative-items", true)) return;

        ItemStack cursor = event.getCursor();
        ItemStack current = event.getCurrentItem();

        if (cursor != null && !cursor.getType().isAir()) {
            String itemName = cursor.getType().name();
            int amount = cursor.getAmount();

            plugin.getDatabaseManager().logCreativeAction(
                    player.getUniqueId().toString(),
                    itemName,
                    amount,
                    "PLACED"
            );
        }

        if (current != null && !current.getType().isAir() && event.getClick() == ClickType.CREATIVE) {
            String itemName = current.getType().name();
            int amount = current.getAmount();

            plugin.getDatabaseManager().logCreativeAction(
                    player.getUniqueId().toString(),
                    itemName,
                    amount,
                    "TAKEN"
            );
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();

        if (player.getGameMode() != GameMode.CREATIVE) return;

        if (!plugin.getConfig().getBoolean("logging.log-creative-items", true)) return;

        if (event.getClick() == ClickType.CREATIVE) {
            ItemStack clicked = event.getCurrentItem();

            if (clicked != null && !clicked.getType().isAir()) {
                String itemName = clicked.getType().name();
                int amount = clicked.getAmount();

                plugin.getDatabaseManager().logCreativeAction(
                        player.getUniqueId().toString(),
                        itemName,
                        amount,
                        "MIDDLE_CLICK"
                );
            }
        }
    }
}