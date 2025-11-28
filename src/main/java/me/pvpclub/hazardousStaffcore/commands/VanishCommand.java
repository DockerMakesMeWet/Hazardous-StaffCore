package me.pvpclub.hazardousStaffcore.commands;

import me.pvpclub.hazardousStaffcore.HazardousStaffcore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VanishCommand implements CommandExecutor, TabCompleter {

    private final HazardousStaffcore plugin;

    public VanishCommand(HazardousStaffcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("staffcore.vanish")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        if (args.length == 0) {
            plugin.getVanishManager().toggleVanish(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("level") && args.length == 2) {
            try {
                int level = Integer.parseInt(args[1]);
                int maxLevel = plugin.getConfig().getInt("vanish.max-level", 5);

                if (level < 1 || level > maxLevel) {
                    player.sendMessage("§cVanish level must be between 1 and " + maxLevel + "!");
                    return true;
                }

                if (!player.hasPermission("staffcore.vanish.level" + level)) {
                    player.sendMessage("§cYou don't have permission to use level " + level + " vanish!");
                    return true;
                }

                plugin.getVanishManager().toggleVanish(player, level);
            } catch (NumberFormatException e) {
                player.sendMessage("§cInvalid level! Usage: /vanish level <1-5>");
            }
            return true;
        }

        player.sendMessage("§cUsage: /vanish or /vanish level <1-5>");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("level");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("level")) {
            completions.addAll(Arrays.asList("1", "2", "3", "4", "5"));
        }

        return completions;
    }
}