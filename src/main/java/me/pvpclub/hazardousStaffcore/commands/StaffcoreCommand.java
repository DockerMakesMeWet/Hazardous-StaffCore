package me.pvpclub.hazardousStaffcore.commands;

import me.pvpclub.hazardousStaffcore.HazardousStaffcore;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StaffcoreCommand implements CommandExecutor, TabCompleter {

    private final HazardousStaffcore plugin;

    public StaffcoreCommand(HazardousStaffcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§c§lHazardousStaffcore");
            player.sendMessage("§e/staffcore logs <player> §7- View command logs");
            player.sendMessage("§e/staffcore creativelogs <player> §7- View creative logs");
            return true;
        }

        if (args[0].equalsIgnoreCase("logs")) {
            if (!player.hasPermission("staffcore.logs")) {
                player.sendMessage("§cYou don't have permission to use this command!");
                return true;
            }

            if (args.length < 2) {
                player.sendMessage("§cUsage: /staffcore logs <player>");
                return true;
            }

            String targetName = args[1];
            plugin.getLogManager().openCommandLogs(player, targetName, 1);
            return true;
        }

        if (args[0].equalsIgnoreCase("creativelogs")) {
            if (!player.hasPermission("staffcore.creativelogs")) {
                player.sendMessage("§cYou don't have permission to use this command!");
                return true;
            }

            if (args.length < 2) {
                player.sendMessage("§cUsage: /staffcore creativelogs <player>");
                return true;
            }

            String targetName = args[1];
            plugin.getLogManager().openCreativeLogs(player, targetName, 1);
            return true;
        }

        player.sendMessage("§cUnknown subcommand!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Arrays.asList("logs", "creativelogs"));
            return completions.stream()
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("logs") || args[0].equalsIgnoreCase("creativelogs"))) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return completions;
    }
}