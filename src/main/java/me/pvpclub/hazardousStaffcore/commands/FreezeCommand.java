package me.pvpclub.hazardousStaffcore.commands;

import me.pvpclub.hazardousStaffcore.HazardousStaffcore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FreezeCommand implements CommandExecutor, TabCompleter {

    private final HazardousStaffcore plugin;

    public FreezeCommand(HazardousStaffcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("staffcore.freeze")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§cUsage: /" + label + " <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage("§cPlayer not found!");
            return true;
        }

        if (label.equalsIgnoreCase("freeze")) {
            plugin.getFreezeManager().freezePlayer(target, player);
        } else if (label.equalsIgnoreCase("unfreeze")) {
            plugin.getFreezeManager().unfreezePlayer(target, player);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}