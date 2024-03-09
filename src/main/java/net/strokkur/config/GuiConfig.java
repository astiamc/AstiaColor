package net.strokkur.config;

import me.clip.placeholderapi.PlaceholderAPI;
import net.strokkur.Main;
import net.strokkur.gui.ChatColorGUI;
import net.strokkur.gui.NameColorGUI;
import net.strokkur.util.GetNickname;
import net.strokkur.util.Pair;
import net.strokkur.util.fastinv.FastInv;
import net.strokkur.util.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuiConfig {

    public static @NotNull FastInv getInventory(String name, Player p) {

        if (cfg.get(name + ".namecolor") != null && cfg.getBoolean(name + ".namecolor")) {
            return NameColorGUI.get(p, name);
        }

        if (cfg.get(name + ".chatcolor") != null && cfg.getBoolean(name + ".chatcolor")) {
            return ChatColorGUI.get(p, name);
        }

        FastInv inv = new FastInv(cfg.getInt(name + ".slots"), getReplaced(name + ".title", p));

        for (Object k : cfg.getKeys(true).stream().filter((str) -> str.contains(name + ".items.") && str.split("\\.").length == 3).toArray()) {
            String key = (String) k;

            Pair<ItemBuilder, Consumer<InventoryClickEvent>> item = getItem(key, p);
            inv.setItem(cfg.getInt(key + ".slot") - 1, item.getKey().build(), item.getValue());
        }

        return inv;
    }


    /*
     * - - - - - - - - - - - - - - - -
     *
     *   Initialisation and util
     *
     * - - - - - - - - - - - - - - - -
     *
     */


    public static YamlConfiguration cfg;
    private static File file;

    private static final File folder = new File("plugins/AstiaColor");


    public static void init() {
        file = new File(folder.getPath() + "/Gui.yml");

        if (!folder.exists()) {
            folder.mkdir();
        }

        if (!file.exists()) {
            try {
                Files.copy(Main.plugin.getResource("Gui.yml"), file.toPath());
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

    public static String getReplaced(String key, Player p) {
        return replace(cfg.getString(key), p);
    }

    private static final Pattern colorify = Pattern.compile("~~.+~~");

    public static String getReplaced(String key, Player p, String color) {
        return replace(cfg.getString(key), p, color);
    }

    public static String replace(String raw, Player p) {
        if (raw.contains("%essentials_nickname_stripped%")) {
            raw = raw.replaceAll("%essentials_nickname_stripped%", GetNickname.get(p));
        }

        if (PlaceholderAPI.containsPlaceholders(raw)) {
            raw = PlaceholderAPI.setPlaceholders(p, raw);
        }

        raw = raw.replaceAll("&", "§");
        raw = raw.replaceAll(Pattern.quote(".._;_.."), "&");

        return raw;
    }

    public static String replace(String raw, Player p, String color) {
        if (raw.contains("%essentials_nickname_stripped%")) {
            raw = raw.replaceAll("%essentials_nickname_stripped%", GetNickname.get(p));
        }

        if (PlaceholderAPI.containsPlaceholders(raw)) {
            raw = PlaceholderAPI.setPlaceholders(p, raw);
        }

        raw = raw.replaceAll("\\{generic_name}", NameColorConfig.getGenericName(color));
        raw = raw.replaceAll("\\{color_array}", NameColorConfig.getColor(color).getColorArray());
        raw = raw.replaceAll("\\{obtained}", NameColorConfig.getObtained(color));

        Matcher colorifyMatcher = colorify.matcher(raw);
        while (colorifyMatcher.find()) {
            raw = colorifyMatcher.replaceFirst(NameColorConfig.getColor(color).colorString(raw.substring(colorifyMatcher.start() + 2, colorifyMatcher.end() - 2)));
            colorifyMatcher = colorify.matcher(raw);
        }

        raw = raw.replaceAll("&", "§");
        raw = raw.replaceAll(Pattern.quote(".._;_.."), "&");

        return raw;
    }

    public static int getColumns(int items) {
        float columnsNeeded = items / 9f;
        String[] split = (String.valueOf(columnsNeeded)).split("\\.");
        int columns = Integer.parseInt(split[0]);
        if (Long.parseLong(split[1]) > 0)
            columns++;
        return columns;
    }

    public static Pair<ItemBuilder, Consumer<InventoryClickEvent>> getItem(String key, Player p) {

        Material material = Material.getMaterial(cfg.getString(key + ".material").toUpperCase());
        ItemBuilder builder;

        if (cfg.get(key + ".data") != null) {
            ItemStack is = new ItemStack(material, (byte) cfg.getInt(key + ".data"));
            builder = new ItemBuilder(is);
        }
        else {
            builder = new ItemBuilder(Material.getMaterial(cfg.getString(key + ".material").toUpperCase()));
        }

        builder.name(getReplaced(key + ".name", p));
        for (String lore : cfg.getStringList(key + ".lore")) {
            builder.addLore(replace(lore, p));
        }

        Consumer<InventoryClickEvent> clickEvent = null;
        if (cfg.get(key + ".onclick.type") != null) {
            String onclick = key + ".onclick.";
            String type = cfg.getString(onclick + "type");
            String value = cfg.getString(onclick + "value");
            boolean close = cfg.get(onclick + "close") != null && cfg.getBoolean(onclick + "close");

            if (type.equalsIgnoreCase("open_inv")) {
                clickEvent = (e) -> {
                    e.setCancelled(true);
                    p.closeInventory();
                    getInventory(value, p).open(p);
                };
            }
            else if (type.equalsIgnoreCase("command")) {
                clickEvent = (e) -> {
                    e.setCancelled(true);
                    boolean executed = p.performCommand(value);
                    if (!executed) {
                        p.sendMessage("§cThere seems to be a problem with this command. Please contact Strokkur24 (@strokkur24 on discord) " +
                                "and send him the following message: 'CMD-ERROR: 404 --- " + onclick + "value'");
                    }
                    if (close) {
                        p.closeInventory();
                    }
                };
            }
            else if (type.equalsIgnoreCase("message")) {
                clickEvent = (e) -> {
                    e.setCancelled(true);
                    p.sendMessage(replace(value, p));
                    if (close) {
                        p.closeInventory();
                    }
                };
            }
        }

        return new Pair<>(builder, clickEvent);
    }
}
