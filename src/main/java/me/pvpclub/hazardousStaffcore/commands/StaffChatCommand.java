package me.pvpclub.hazardousStaffcore.commands;

import me.pvpclub.hazardousStaffcore.HazardousStaffcore;
import me.pvpclub.hazardousStaffcore.managers.ChatManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StaffChatCommand implements CommandExecutor, TabCompleter {

    private final HazardousStaffcore plugin;

    public StaffChatCommand(HazardousStaffcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        String channelKey = getChannelFromCommand(label);
        ChatManager.ChatChannel channel = plugin.getChatManager().getChannel(channelKey);

        if (channel == null) {
            player.sendMessage("§cChat channel not found!");
            return true;
        }

        if (!player.hasPermission(channel.getPermission())) {
            player.sendMessage("§cYou don't have permission to use this chat!");
            return true;
        }

        plugin.getChatManager().toggleChat(player, channelKey);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }

    private String getChannelFromCommand(String label) {
        switch (label.toLowerCase()) {
            case "sc":
            case "staffchat":
                return "staffchat";
            case "dc":
            case "devchat":
                return "devchat";
            case "adminchat":
                return "adminchat";
            case "managementchat":
                return "managementchat";
            case "donatorchat":
                return "donatorchat";
            default:
                return label.toLowerCase();
        }
    }
}