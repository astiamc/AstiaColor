package net.strokkur.config;

import javafx.util.Pair;
import me.clip.placeholderapi.PlaceholderAPI;
import net.strokkur.Main;
import net.strokkur.color.Database;
import net.strokkur.util.fastinv.FastInv;
import net.strokkur.util.fastinv.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuiConfig {

    public static @NotNull FastInv getInventory(String name, Player p) {

        if (cfg.get(name + ".colors") != null && cfg.getBoolean(name + ".colors")) {

            int colorAmount = ColorConfig.getAllColors().size();
            colorAmount += cfg.getBoolean(name + ".enable-items") ? 9 : 0;

            if (colorAmount > 54) {
                p.sendMessage("§cThere seems to be a problem with the color gui. Please contact Strokkur24 (@strokkur24 on discord) " +
                        "and send him the following message: 'CONFIG-ERROR: 207 --- Expected a number less or equal than 54, found " + colorAmount + "'");
            }

            FastInv inv = new FastInv(getColumns(colorAmount) * 9, getReplaced(name + ".title", p));

            if (cfg.getBoolean(name + ".enable-items")) {
                for (Object k : cfg.getKeys(true).stream().filter((str) -> str.contains(name + ".items.")).toArray()) {
                    String key = (String) k;

                    if (cfg.getInt(key + "slot") > 9) {
                        continue;
                    }

                    Pair<ItemBuilder, Consumer<InventoryClickEvent>> item = getItem(key, p);
                    inv.setItem(cfg.getInt(key + ".slot") + inv.getInventory().getSize() - 10, item.getKey().build(), item.getValue());
                }
            }

            int slot = 0;
            for (String c : ColorConfig.getAllColors()) {

                ItemBuilder builder = new ItemBuilder(ColorConfig.getMaterial(c));
                builder.name(getReplaced(name + ".colors-display", p, c));

                Consumer<InventoryClickEvent> clickEvent = (e) -> {
                    e.setCancelled(true);

                    for (String str : cfg.getStringList(name + ".colors-messages.locked")) {
                        p.sendMessage(replace(str, p, c));
                    }
                };

                if (p.hasPermission(ColorConfig.getPermission(c))) {
                    for (String str : cfg.getStringList(name + ".colors-lore.owned")) {
                        str = replace(str, p, c);
                        builder.addLore(str);
                    }
                    clickEvent = (e) -> {
                        e.setCancelled(true);
                        Database.setColor(p.getUniqueId(), c);

                        for (String str : cfg.getStringList(name + ".colors-messages.equipped")) {
                            p.sendMessage(replace(str, p, c));
                        }
                    };
                }
                else {
                    for (String str : cfg.getStringList(name + ".colors-lore.locked")) {
                        str = replace(str, p, c);
                        builder.addLore(str);
                    }
                }

                inv.setItem(slot, builder.build(), clickEvent);
                slot++;
            }

        }

        FastInv inv = new FastInv(cfg.getInt(name + ".slots"), getReplaced(name + ".title", p));

        for (Object k : cfg.getKeys(true).stream().filter((str) -> str.contains(name + ".items.")).toArray()) {
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


    private static YamlConfiguration cfg;
    private static File file;


    public static void init() {
        file = new File(Main.plugin.getDataFolder() + "\\gui.yml");

        if (Main.plugin.getDataFolder().exists()) {
            Main.plugin.getDataFolder().mkdir();
        }

        try {
            Files.copy(Objects.requireNonNull(Main.plugin.getResource("gui.yml")),
                    file.toPath());
        }
        catch (IOException e) {
            e.printStackTrace();
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
        if (PlaceholderAPI.containsPlaceholders(raw)) {
            raw = PlaceholderAPI.setPlaceholders(p, raw);
        }

        raw = ChatColor.translateAlternateColorCodes('&', raw);
        raw = raw.replaceAll(Pattern.quote(".._;_.."), "&");

        return raw;
    }

    public static String replace(String raw, Player p, String color) {
        raw = raw.replaceAll("\\{generic_name}", ColorConfig.getGenericName(color));
        raw = raw.replaceAll("\\{color_array}", ColorConfig.getColor(color).getColorArray());
        raw = raw.replaceAll("\\{obtained}", ColorConfig.getObtained(color));

        if (PlaceholderAPI.containsPlaceholders(raw)) {
            raw = PlaceholderAPI.setPlaceholders(p, raw);
        }

        Matcher colorifyMatcher = colorify.matcher(raw);
        while (colorifyMatcher.find()) {
            raw = colorifyMatcher.replaceFirst(raw.substring(colorifyMatcher.start() + 2, colorifyMatcher.regionEnd() - 2));
            colorifyMatcher = colorify.matcher(raw);
        }

        raw = ChatColor.translateAlternateColorCodes('&', raw);
        raw = raw.replaceAll(Pattern.quote(".._;_.."), "&");

        return raw;
    }

    private static int getColumns(int items) {
        float columnsNeeded = items / 7f;
        String[] split = (String.valueOf(columnsNeeded)).split("\\.");
        int columns = Integer.parseInt(split[0]);
        if (Long.parseLong(split[1]) > 0)
            columns++;
        return columns;
    }

    public static Pair<ItemBuilder, Consumer<InventoryClickEvent>> getItem(String key, Player p) {
        ItemBuilder builder = new ItemBuilder(Material.getMaterial(cfg.getString(key + ".material").toUpperCase()));
        builder.name(getReplaced(key + ".name", p));
        for (String lore : cfg.getStringList(key + "lore")) {
            builder.addLore(replace(lore, p));
        }

        Consumer<InventoryClickEvent> clickEvent = null;
        if (cfg.get(key + ".onclick.type") != null) {
            String onclick = key + ".onclick.";
            String type = cfg.getString(onclick + "type");
            String value = cfg.getString(onclick + "value");
            boolean close = cfg.get(onclick + "close") != null && cfg.getBoolean(onclick + "close");

            if (key.equalsIgnoreCase("open_inv")) {
                clickEvent = (e) -> {
                    e.setCancelled(true);
                    getInventory(value, p).open(p);
                };
            }
            else if (key.equalsIgnoreCase("command")) {
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
            else if (key.equalsIgnoreCase("message")) {
                clickEvent = (e) -> {
                    e.setCancelled(true);
                    p.sendMessage(value);
                    if (close) {
                        p.closeInventory();
                    }
                };
            }
        }

        return new Pair<>(builder, clickEvent);
    }
}
