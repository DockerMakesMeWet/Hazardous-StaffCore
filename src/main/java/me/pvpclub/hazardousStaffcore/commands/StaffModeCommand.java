package me.pvpclub.hazardousStaffcore.commands;

import me.pvpclub.hazardousStaffcore.HazardousStaffcore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StaffModeCommand implements CommandExecutor, TabCompleter {

    private final HazardousStaffcore plugin;

    public StaffModeCommand(HazardousStaffcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("staffcore.staffmode")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        plugin.getStaffModeManager().toggleStaffMode(player);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}