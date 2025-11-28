package me.pvpclub.hazardousStaffcore.utils;

import me.pvpclub.hazardousStaffcore.HazardousStaffcore;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private final HazardousStaffcore plugin;
    private Connection connection;
    private String host, database, username, password;
    private int port;

    public DatabaseManager(HazardousStaffcore plugin) {
        this.plugin = plugin;
        this.host = plugin.getConfig().getString("mysql.host");
        this.port = plugin.getConfig().getInt("mysql.port");
        this.database = plugin.getConfig().getString("mysql.database");
        this.username = plugin.getConfig().getString("mysql.username");
        this.password = plugin.getConfig().getString("mysql.password");
    }

    public void connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }

            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false", username, password);
            createTables();
            plugin.getLogger().info("Successfully connected to MySQL database!");
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to MySQL database!");
            plugin.getLogger().severe("Please check your database credentials in config.yml");
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                plugin.getLogger().info("Disconnected from MySQL database!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void createTables() {
        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS staff_chat (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "chat_type VARCHAR(50)," +
                    "player VARCHAR(36)," +
                    "message TEXT," +
                    "timestamp BIGINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS command_logs (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "player VARCHAR(36)," +
                    "command TEXT," +
                    "timestamp BIGINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS creative_logs (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "player VARCHAR(36)," +
                    "item VARCHAR(255)," +
                    "amount INT," +
                    "action VARCHAR(50)," +
                    "timestamp BIGINT)");

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void logStaffChat(String chatType, String player, String message) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = getConnection().prepareStatement(
                        "INSERT INTO staff_chat (chat_type, player, message, timestamp) VALUES (?, ?, ?, ?)");
                statement.setString(1, chatType);
                statement.setString(2, player);
                statement.setString(3, message);
                statement.setLong(4, System.currentTimeMillis());
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void logCommand(String player, String command) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = getConnection().prepareStatement(
                        "INSERT INTO command_logs (player, command, timestamp) VALUES (?, ?, ?)");
                statement.setString(1, player);
                statement.setString(2, command);
                statement.setLong(3, System.currentTimeMillis());
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void logCreativeAction(String player, String item, int amount, String action) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = getConnection().prepareStatement(
                        "INSERT INTO creative_logs (player, item, amount, action, timestamp) VALUES (?, ?, ?, ?, ?)");
                statement.setString(1, player);
                statement.setString(2, item);
                statement.setInt(3, amount);
                statement.setString(4, action);
                statement.setLong(5, System.currentTimeMillis());
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public List<String> getCommandLogs(String player, int page, int perPage) {
        List<String> logs = new ArrayList<>();
        try {
            PreparedStatement statement = getConnection().prepareStatement(
                    "SELECT command, timestamp FROM command_logs WHERE player = ? ORDER BY timestamp DESC LIMIT ? OFFSET ?");
            statement.setString(1, player);
            statement.setInt(2, perPage);
            statement.setInt(3, (page - 1) * perPage);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                logs.add(rs.getString("command") + "|" + rs.getLong("timestamp"));
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    public int getCommandLogsCount(String player) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(
                    "SELECT COUNT(*) FROM command_logs WHERE player = ?");
            statement.setString(1, player);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                statement.close();
                return count;
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<String> getCreativeLogs(String player, int page, int perPage) {
        List<String> logs = new ArrayList<>();
        try {
            PreparedStatement statement = getConnection().prepareStatement(
                    "SELECT item, amount, action, timestamp FROM creative_logs WHERE player = ? ORDER BY timestamp DESC LIMIT ? OFFSET ?");
            statement.setString(1, player);
            statement.setInt(2, perPage);
            statement.setInt(3, (page - 1) * perPage);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                logs.add(rs.getString("item") + "|" + rs.getInt("amount") + "|" +
                        rs.getString("action") + "|" + rs.getLong("timestamp"));
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    public int getCreativeLogsCount(String player) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(
                    "SELECT COUNT(*) FROM creative_logs WHERE player = ?");
            statement.setString(1, player);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                statement.close();
                return count;
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}