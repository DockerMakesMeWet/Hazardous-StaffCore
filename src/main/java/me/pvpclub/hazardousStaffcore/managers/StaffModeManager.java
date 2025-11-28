package me.pvpclub.hazardousStaffcore.managers;

import me.pvpclub.hazardousStaffcore.HazardousStaffcore;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class StaffModeManager {

    private final HazardousStaffcore plugin;
    private Set<UUID> staffMode;
    private Map<UUID, ItemStack[]> savedInventories;
    private Map<UUID, ItemStack[]> savedArmor;
    private Map<UUID, GameMode> savedGameMode;

    public StaffModeManager(HazardousStaffcore plugin) {
        this.plugin = plugin;
        this.staffMode = new HashSet<>();
        this.savedInventories = new HashMap<>();
        this.savedArmor = new HashMap<>();
        this.savedGameMode = new HashMap<>();
    }

    public void toggleStaffMode(Player player) {
        UUID uuid = player.getUniqueId();

        if (staffMode.contains(uuid)) {
            disableStaffMode(player);
        } else {
            enableStaffMode(player);
        }
    }

    public void enableStaffMode(Player player) {
        UUID uuid = player.getUniqueId();

        savedInventories.put(uuid, player.getInventory().getContents());
        savedArmor.put(uuid, player.getInventory().getArmorContents());
        savedGameMode.put(uuid, player.getGameMode());

        staffMode.add(uuid);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.setAllowFlight(true);
        player.setFlying(true);
        player.setInvulnerable(true);

        giveStaffItems(player);

        player.sendMessage("§aStaff mode enabled!");
    }

    public void disableStaffMode(Player player) {
        UUID uuid = player.getUniqueId();

        staffMode.remove(uuid);

        player.getInventory().clear();

        if (savedInventories.containsKey(uuid)) {
            player.getInventory().setContents(savedInventories.get(uuid));
            savedInventories.remove(uuid);
        }

        if (savedArmor.containsKey(uuid)) {
            player.getInventory().setArmorContents(savedArmor.get(uuid));
            savedArmor.remove(uuid);
        }

        if (savedGameMode.containsKey(uuid)) {
            player.setGameMode(savedGameMode.get(uuid));
            savedGameMode.remove(uuid);
        }

        player.setAllowFlight(false);
        player.setFlying(false);
        player.setInvulnerable(false);

        player.sendMessage("§cStaff mode disabled!");
    }

    private void giveStaffItems(Player player) {
        ItemStack freezeItem = new ItemStack(Material.ICE);
        ItemMeta freezeMeta = freezeItem.getItemMeta();
        freezeMeta.setDisplayName("§b§lFreeze Player");
        freezeMeta.setLore(Arrays.asList("§7Left click a player to freeze them", "§7Click again to unfreeze"));
        freezeItem.setItemMeta(freezeMeta);

        ItemStack inspectItem = new ItemStack(Material.BOOK);
        ItemMeta inspectMeta = inspectItem.getItemMeta();
        inspectMeta.setDisplayName("§e§lInspect Inventory");
        inspectMeta.setLore(Arrays.asList("§7Right click a player to view", "§7their inventory"));
        inspectItem.setItemMeta(inspectMeta);

        ItemStack teleportItem = new ItemStack(Material.COMPASS);
        ItemMeta teleportMeta = teleportItem.getItemMeta();
        teleportMeta.setDisplayName("§a§lRandom Teleport");
        teleportMeta.setLore(Arrays.asList("§7Right click to teleport", "§7to a random player"));
        teleportItem.setItemMeta(teleportMeta);

        ItemStack vanishItem = new ItemStack(Material.GRAY_DYE);
        ItemMeta vanishMeta = vanishItem.getItemMeta();
        vanishMeta.setDisplayName("§7§lVanish Toggle");
        vanishMeta.setLore(Arrays.asList("§7Click to toggle vanish"));
        vanishItem.setItemMeta(vanishMeta);

        player.getInventory().setItem(0, freezeItem);
        player.getInventory().setItem(1, inspectItem);
        player.getInventory().setItem(4, teleportItem);
        player.getInventory().setItem(8, vanishItem);
    }

    public boolean isInStaffMode(Player player) {
        return staffMode.contains(player.getUniqueId());
    }
}