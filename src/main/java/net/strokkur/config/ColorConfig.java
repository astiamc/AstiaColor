package net.strokkur.config;

import net.strokkur.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Set;

public class ColorConfig {
    public static Set<String> getAllColors() {
        return cfg.getKeys(false);
    }
    public static NameColor getColor(String color) {
        return new NameColor(color, Objects.requireNonNull(cfg.getStringList(color + ".colors")));
    }
    public static String genericName(String color) {
        return Objects.requireNonNull(cfg.getString(color + ".genericName"));
    }
    public static String getPermission(String color) {
        return Objects.requireNonNull(cfg.getString(color + ".permission"));
    }
    public static Material getMaterial(String color) {
        return Material.getMaterial(color.toUpperCase());
    }


    /*
     * - - - - - - - - - - - - - - - -
     *
     *   Initialisation and util
     *
     * - - - - - - - - - - - - - - - -
     *
     */


    private static YamlConfiguration cfg;
    private static File file;


    public ColorConfig(String fileName) {
        file = new File(Main.plugin.getDataFolder() + "\\" + fileName);

        if (Main.plugin.getDataFolder().exists()) {
            Main.plugin.getDataFolder().mkdir();
        }

        try {
            Files.copy(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("colors.yml")),
                    file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        reload();
    }

    public static void reload() {
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public static void save() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            ;
        }
    }
}
