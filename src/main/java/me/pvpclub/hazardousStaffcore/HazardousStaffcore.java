package me.pvpclub.hazardousStaffcore;

import me.pvpclub.hazardousStaffcore.commands.*;
import me.pvpclub.hazardousStaffcore.listeners.*;
import me.pvpclub.hazardousStaffcore.managers.*;
import me.pvpclub.hazardousStaffcore.utils.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HazardousStaffcore extends JavaPlugin {

    private static HazardousStaffcore instance;
    private DatabaseManager databaseManager;
    private VanishManager vanishManager;
    private FreezeManager freezeManager;
    private StaffModeManager staffModeManager;
    private ChatManager chatManager;
    private LogManager logManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        saveResource("chats.yml", false);

        databaseManager = new DatabaseManager(this);
        databaseManager.connect();

        vanishManager = new VanishManager(this);
        freezeManager = new FreezeManager(this);
        staffModeManager = new StaffModeManager(this);
        chatManager = new ChatManager(this);
        logManager = new LogManager(this);

        registerCommands();
        registerListeners();

        getLogger().info("HazardousStaffcore has been enabled!");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.disconnect();
        }
        getLogger().info("HazardousStaffcore has been disabled!");
    }

    private void registerCommands() {
        StaffChatCommand chatCommand = new StaffChatCommand(this);
        getCommand("sc").setExecutor(chatCommand);
        getCommand("sc").setTabCompleter(chatCommand);
        getCommand("staffchat").setExecutor(chatCommand);
        getCommand("staffchat").setTabCompleter(chatCommand);
        getCommand("dc").setExecutor(chatCommand);
        getCommand("dc").setTabCompleter(chatCommand);
        getCommand("devchat").setExecutor(chatCommand);
        getCommand("devchat").setTabCompleter(chatCommand);
        getCommand("adminchat").setExecutor(chatCommand);
        getCommand("adminchat").setTabCompleter(chatCommand);
        getCommand("managementchat").setExecutor(chatCommand);
        getCommand("managementchat").setTabCompleter(chatCommand);
        getCommand("donatorchat").setExecutor(chatCommand);
        getCommand("donatorchat").setTabCompleter(chatCommand);

        VanishCommand vanishCommand = new VanishCommand(this);
        getCommand("vanish").setExecutor(vanishCommand);
        getCommand("vanish").setTabCompleter(vanishCommand);
        getCommand("v").setExecutor(vanishCommand);
        getCommand("v").setTabCompleter(vanishCommand);

        FreezeCommand freezeCommand = new FreezeCommand(this);
        getCommand("freeze").setExecutor(freezeCommand);
        getCommand("freeze").setTabCompleter(freezeCommand);
        getCommand("unfreeze").setExecutor(freezeCommand);
        getCommand("unfreeze").setTabCompleter(freezeCommand);

        StaffModeCommand staffModeCommand = new StaffModeCommand(this);
        getCommand("staffmode").setExecutor(staffModeCommand);
        getCommand("staffmode").setTabCompleter(staffModeCommand);

        StaffcoreCommand staffcoreCommand = new StaffcoreCommand(this);
        getCommand("staffcore").setExecutor(staffcoreCommand);
        getCommand("staffcore").setTabCompleter(staffcoreCommand);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new VanishListener(this), this);
        getServer().getPluginManager().registerEvents(new FreezeListener(this), this);
        getServer().getPluginManager().registerEvents(new StaffModeListener(this), this);
        getServer().getPluginManager().registerEvents(new CommandLogListener(this), this);
        getServer().getPluginManager().registerEvents(new CreativeLogListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
    }

    public static HazardousStaffcore getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public VanishManager getVanishManager() {
        return vanishManager;
    }

    public FreezeManager getFreezeManager() {
        return freezeManager;
    }

    public StaffModeManager getStaffModeManager() {
        return staffModeManager;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public LogManager getLogManager() {
        return logManager;
    }
}