package me.pvpclub.hazardousStaffcore.managers;

import me.pvpclub.hazardousStaffcore.HazardousStaffcore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class FreezeManager {

    private final HazardousStaffcore plugin;
    private Set<UUID> frozenPlayers;
    private Map<UUID, Location> originalLocations;

    public FreezeManager(HazardousStaffcore plugin) {
        this.plugin = plugin;
        this.frozenPlayers = new HashSet<>();
        this.originalLocations = new HashMap<>();
    }

    public void freezePlayer(Player player, Player staff) {
        UUID uuid = player.getUniqueId();

        if (frozenPlayers.contains(uuid)) {
            staff.sendMessage("§c" + player.getName() + " is already frozen!");
            return;
        }

        frozenPlayers.add(uuid);
        originalLocations.put(uuid, player.getLocation());

        if (plugin.getConfig().getBoolean("freeze.teleport-up", true)) {
            Location tpLoc = player.getLocation().clone();
            tpLoc.setY(200);
            player.teleport(tpLoc);
        }

        String title = plugin.getConfig().getString("freeze.title", "§c§lFROZEN");
        String subtitle = plugin.getConfig().getString("freeze.subtitle", "§eYou have been frozen! Do not leave or you will be punished!");

        player.sendTitle(title, subtitle, 10, 70, 20);

        startFreezeSubtitleLoop(player);

        player.sendMessage("§c§l[!] You have been frozen by " + staff.getName() + "!");
        player.sendMessage("§c§l[!] Do not disconnect or you will be punished!");

        staff.sendMessage("§aYou have frozen " + player.getName() + ".");
    }

    public void unfreezePlayer(Player player, Player staff) {
        UUID uuid = player.getUniqueId();

        if (!frozenPlayers.contains(uuid)) {
            staff.sendMessage("§c" + player.getName() + " is not frozen!");
            return;
        }

        frozenPlayers.remove(uuid);
        originalLocations.remove(uuid);

        player.sendTitle("§a§lUNFROZEN", "§eYou have been unfrozen!", 10, 70, 20);
        player.sendMessage("§aYou have been unfrozen by " + staff.getName() + ".");

        staff.sendMessage("§aYou have unfrozen " + player.getName() + ".");
    }

    public boolean isFrozen(Player player) {
        return frozenPlayers.contains(player.getUniqueId());
    }

    private void startFreezeSubtitleLoop(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || !isFrozen(player)) {
                    cancel();
                    return;
                }

                String subtitle = plugin.getConfig().getString("freeze.subtitle", "§eYou have been frozen! Do not leave or you will be punished!");
                player.sendTitle("", subtitle, 0, 60, 10);
            }
        }.runTaskTimer(plugin, 60L, 60L);
    }

    public void handleQuit(Player player) {
        if (isFrozen(player)) {
            String playerName = player.getName();
            UUID uuid = player.getUniqueId();

            Bukkit.broadcast("§c§l[!] " + playerName + " left while frozen!", "staffcore.freeze.notify");

            frozenPlayers.remove(uuid);
            originalLocations.remove(uuid);
        }
    }
}