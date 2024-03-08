package net.strokkur.config;

import net.strokkur.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
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

    public static String getGenericName(String color) {
        return Objects.requireNonNull(cfg.getString(color + ".genericName"));
    }

    public static String getPermission(String color) {
        return Objects.requireNonNull(cfg.getString(color + ".permission"));
    }

    public static Material getMaterial(String color) {
        Material out = Material.getMaterial(cfg.getString(color + ".item").toUpperCase());

        if (out == null) {
            throw new RuntimeException(cfg.getString(color + ".item").toUpperCase() + " is not a valid material. >>> Colors.yml:" + color + ".item");
        }

        return Material.getMaterial(cfg.getString(color + ".item").toUpperCase());
    }

    public static String getObtained(String color) {
        return Objects.requireNonNull(cfg.getString(color + ".obtained"));
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

    private static final File folder = new File("plugins/AstiaColor");


    public static void init() {
        file = new File(folder.getPath() + "/Colors.yml");

        if (!folder.exists()) {
            folder.mkdir();
        }

        if (!file.exists()) {
            try {
                Files.copy(Main.plugin.getResource("Colors.yml"), file.toPath());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        reload();
    }

    public static void reload() {
        cfg = YamlConfiguration.loadConfiguration(file);
    }
}
