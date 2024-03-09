package net.strokkur.color.config;

import net.strokkur.color.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Set;

public class ChatColorConfig {
    public static Set<String> getAllColors() {
        return cfg.getKeys(false);
    }

    public static String getColor(String color) {
        return cfg.getString(color + ".color");
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
            throw new RuntimeException(cfg.getString(color + ".item").toUpperCase() + " is not a valid material. >>> NameColors.yml:" + color + ".item");
        }

        return Material.getMaterial(cfg.getString(color + ".item").toUpperCase());
    }

    public static String getObtained(String color) {
        return Objects.requireNonNull(cfg.getString(color + ".obtained"));
    }

    public static boolean isVisible(Player p, String color) {
        return p.hasPermission(cfg.getString(color + ".permission")) || cfg.getBoolean(color + ".visible");
    }

    public static boolean hasData(String color) {
        return cfg.get(color + ".data") != null;
    }
    public static byte getData(String color) {
        return Byte.parseByte(cfg.getString(color + ".data"));
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
        file = new File(folder.getPath() + "/ChatColors.yml");

        if (!folder.exists()) {
            folder.mkdir();
        }

        if (!file.exists()) {
            try {
                Files.copy(Main.plugin.getResource("ChatColors.yml"), file.toPath());
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
