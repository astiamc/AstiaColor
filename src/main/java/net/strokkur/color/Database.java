package net.strokkur.color;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


public class Database {
    private static final File folder = new File("plugins/AstiaColor");
    private static final File file = new File("plugins/AstiaColor/ActiveColors.yml");
    private static YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

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
        }
    }

    public static @Nullable String getNameColor(UUID uuid) {
        if (cfg.get(uuid.toString() + ".name") == null)
            return "default";

        return cfg.getString(uuid + ".name");
    }

    public static boolean hasNoNameColor(UUID uuid) {
        return cfg.get(uuid.toString() + ".name") == null;
    }

    public static void setNameColor(UUID uuid, String color) {
        cfg.set(uuid.toString() + ".name", color.equalsIgnoreCase("default") ? null : color);
        save();
    }
    public static @Nullable String getChatColor(UUID uuid) {
        if (cfg.get(uuid.toString() + ".color") == null)
            return "default";

        return cfg.getString(uuid.toString() + ".color");
    }

    public static boolean hasNoChatColor(UUID uuid) {
        return cfg.get(uuid.toString() + ".color") == null;
    }

    public static void setChatColor(UUID uuid, String color) {
        cfg.set(uuid.toString() + ".color", color.equalsIgnoreCase("default") ? null : color);
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
