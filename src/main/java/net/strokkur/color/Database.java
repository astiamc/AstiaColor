package net.strokkur.color;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


public class Database {

    //private static Connection connection;
    private static final File folder = new File("plugins/AstiaColor");
    private static final File file = new File("plugins/AstiaColor/ActiveColors.yml");
    private static YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    //private static final String path = folder.getAbsolutePath() + "\\playernamecolors.db";
    // private static String connectionString = "jdbc:sqlite:" + path;

    public static void init() {

        if (!folder.exists()) {
            folder.mkdir();
        }
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            ;
        }


        /*
        try {
            if (!folder.exists()) {
                folder.mkdir();
            }

            connection = DriverManager.getConnection(connectionString);
            if (connection != null) {
                Bukkit.getConsoleSender().sendMessage("§8[§5§lD§d§lB§8]§d Successfully connected to SQLite-Database");
            }

            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS ColorTable (uuid text, color text);");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    public static @Nullable String getColor(UUID uuid) {
        /*
        try {
            if (contains(uuid)) return null;

            ResultSet set = connection.createStatement().executeQuery("SELECT * FROM ColorTable WHERE uuid = '" + uuid + "'");
            return set.getString("color");

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;*/

        if (cfg.get(uuid.toString()) == null)
            return "default";

        return cfg.getString(uuid.toString());
    }

    public static boolean doesNotContain(UUID uuid) {
        /*
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM ColorTable WHERE uuid = '" + uuid + "'");
            return resultSet.getString("color") != null;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;*/
        return cfg.get(uuid.toString()) == null;
    }

    public static void setColor(UUID uuid, String color) {
        /*
        try {
            if (contains(uuid)) {
                connection.createStatement().executeQuery(String.format("UPDATE ColorTable SET color = '%s' WHERE uuid = '%s'", color, uuid));
                return;
            }

            connection.createStatement().executeQuery(String.format("INSERT INTO ColorTable (uuid, color) VALUES ('%s', '%s')", uuid, color));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }*/

        cfg.set(uuid.toString(), color.equalsIgnoreCase("default") ? null : color);
        save();
    }

    public static void save() {
        try {
            cfg.save(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
