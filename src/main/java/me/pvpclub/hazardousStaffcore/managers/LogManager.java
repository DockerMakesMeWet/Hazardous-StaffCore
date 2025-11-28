package me.pvpclub.hazardousStaffcore.managers;

import me.pvpclub.hazardousStaffcore.HazardousStaffcore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.*;

public class LogManager {

    private final HazardousStaffcore plugin;
    private final Map<UUID, Integer> currentPages;

    public LogManager(HazardousStaffcore plugin) {
        this.plugin = plugin;
        this.currentPages = new HashMap<>();
    }

    public void openCommandLogs(Player viewer, String targetName, int page) {
        final String finalTargetName = targetName;
        final int finalPage = page;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            OfflinePlayer target = Bukkit.getOfflinePlayer(finalTargetName);

            List<String> logs = plugin.getDatabaseManager().getCommandLogs(target.getUniqueId().toString(), finalPage, 28);
            int totalLogs = plugin.getDatabaseManager().getCommandLogsCount(target.getUniqueId().toString());
            int maxPages = (int) Math.ceil(totalLogs / 28.0);

            final int finalMaxPages = maxPages == 0 ? 1 : maxPages;

            Bukkit.getScheduler().runTask(plugin, () -> {
                Inventory inv = Bukkit.createInventory(null, 54, "§c§lCommand Logs - " + finalTargetName + " (Page " + finalPage + "/" + finalMaxPages + ")");

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

                int slot = 0;
                for (String log : logs) {
                    String[] parts = log.split("\\|");
                    String command = parts[0];
                    long timestamp = Long.parseLong(parts[1]);

                    ItemStack item = new ItemStack(Material.PAPER);
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        meta.setDisplayName("§e/" + command);
                        meta.setLore(Arrays.asList(
                                "§7Time: §f" + sdf.format(new Date(timestamp)),
                                "§7Command: §f/" + command
                        ));
                        item.setItemMeta(meta);
                    }

                    inv.setItem(slot++, item);
                }

                if (finalPage > 1) {
                    ItemStack prevPage = new ItemStack(Material.ARROW);
                    ItemMeta prevMeta = prevPage.getItemMeta();
                    if (prevMeta != null) {
                        prevMeta.setDisplayName("§a§lPrevious Page");
                        prevPage.setItemMeta(prevMeta);
                    }
                    inv.setItem(45, prevPage);
                }

                if (finalPage < finalMaxPages) {
                    ItemStack nextPage = new ItemStack(Material.ARROW);
                    ItemMeta nextMeta = nextPage.getItemMeta();
                    if (nextMeta != null) {
                        nextMeta.setDisplayName("§a§lNext Page");
                        nextPage.setItemMeta(nextMeta);
                    }
                    inv.setItem(53, nextPage);
                }

                ItemStack close = new ItemStack(Material.BARRIER);
                ItemMeta closeMeta = close.getItemMeta();
                if (closeMeta != null) {
                    closeMeta.setDisplayName("§c§lClose");
                    close.setItemMeta(closeMeta);
                }
                inv.setItem(49, close);

                currentPages.put(viewer.getUniqueId(), finalPage);
                viewer.openInventory(inv);
            });
        });
    }

    public void openCreativeLogs(Player viewer, String targetName, int page) {
        final String finalTargetName = targetName;
        final int finalPage = page;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            OfflinePlayer target = Bukkit.getOfflinePlayer(finalTargetName);

            List<String> logs = plugin.getDatabaseManager().getCreativeLogs(target.getUniqueId().toString(), finalPage, 28);
            int totalLogs = plugin.getDatabaseManager().getCreativeLogsCount(target.getUniqueId().toString());
            int maxPages = (int) Math.ceil(totalLogs / 28.0);

            final int finalMaxPages = maxPages == 0 ? 1 : maxPages;

            Bukkit.getScheduler().runTask(plugin, () -> {
                Inventory inv = Bukkit.createInventory(null, 54, "§6§lCreative Logs - " + finalTargetName + " (Page " + finalPage + "/" + finalMaxPages + ")");

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

                int slot = 0;
                for (String log : logs) {
                    String[] parts = log.split("\\|");
                    String itemName = parts[0];
                    int amount = Integer.parseInt(parts[1]);
                    String action = parts[2];
                    long timestamp = Long.parseLong(parts[3]);

                    Material material = Material.getMaterial(itemName);
                    if (material == null) material = Material.PAPER;

                    ItemStack item = new ItemStack(material);
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        meta.setDisplayName("§e" + itemName);
                        meta.setLore(Arrays.asList(
                                "§7Time: §f" + sdf.format(new Date(timestamp)),
                                "§7Action: §f" + action,
                                "§7Amount: §f" + amount
                        ));
                        item.setItemMeta(meta);
                    }

                    inv.setItem(slot++, item);
                }

                if (finalPage > 1) {
                    ItemStack prevPage = new ItemStack(Material.ARROW);
                    ItemMeta prevMeta = prevPage.getItemMeta();
                    if (prevMeta != null) {
                        prevMeta.setDisplayName("§a§lPrevious Page");
                        prevPage.setItemMeta(prevMeta);
                    }
                    inv.setItem(45, prevPage);
                }

                if (finalPage < finalMaxPages) {
                    ItemStack nextPage = new ItemStack(Material.ARROW);
                    ItemMeta nextMeta = nextPage.getItemMeta();
                    if (nextMeta != null) {
                        nextMeta.setDisplayName("§a§lNext Page");
                        nextPage.setItemMeta(nextMeta);
                    }
                    inv.setItem(53, nextPage);
                }

                ItemStack close = new ItemStack(Material.BARRIER);
                ItemMeta closeMeta = close.getItemMeta();
                if (closeMeta != null) {
                    closeMeta.setDisplayName("§c§lClose");
                    close.setItemMeta(closeMeta);
                }
                inv.setItem(49, close);

                currentPages.put(viewer.getUniqueId(), finalPage);
                viewer.openInventory(inv);
            });
        });
    }

    public int getCurrentPage(UUID uuid) {
        return currentPages.getOrDefault(uuid, 1);
    }
}