package net.strokkur.color;

import net.strokkur.Main;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.UUID;

public class Database {

    private static Connection connection;
    private static final String path = Main.plugin.getDataFolder().getPath() + "/playercolors.db";
    private static String connectionString = "jdbc:sqlite:" + path;

    static {
        try {
            if (!Main.plugin.getDataFolder().exists()) {
                Main.plugin.getDataFolder().mkdir();
            }

            connection = DriverManager.getConnection(connectionString);
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS ColorTable (uuid text, color text);");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static @Nullable String getColor(UUID uuid) {
        try {
            if (contains(uuid)) return null;

            ResultSet set = connection.createStatement().executeQuery("SELECT * FROM ColorTable WHERE uuid = '" + uuid + "'");
            return set.getString("color");

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Boolean contains(UUID uuid) {
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM ColorTable WHERE uuid = '" + uuid + "'");
            return resultSet.getString("color") != null;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void setColor(UUID uuid, String color) {
        try {
            if (contains(uuid)) {
                connection.createStatement().executeQuery(String.format("UPDATE ColorTable SET color = '%s' WHERE uuid = '%s'", color, uuid));
                return;
            }

            connection.createStatement().executeQuery(String.format("INSERT INTO ColorTable (uuid, color) VALUES ('%s', '%s')", uuid, color));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
