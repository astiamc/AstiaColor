package net.strokkur.gui;

import me.clip.placeholderapi.PlaceholderAPI;
import net.strokkur.color.Database;
import net.strokkur.config.ChatColorConfig;
import net.strokkur.config.NameColorConfig;
import net.strokkur.util.Pair;
import net.strokkur.util.fastinv.FastInv;
import net.strokkur.util.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static net.strokkur.config.GuiConfig.cfg;
import static net.strokkur.config.GuiConfig.getColumns;
import static net.strokkur.config.GuiConfig.getItem;

public class ChatColorGUI {    
    public static FastInv get(Player p, String name) {
        int colorAmount = ChatColorConfig.getAllColors().size();
        colorAmount += cfg.getBoolean(name + ".enable-items") ? 9 : 0;

        if (colorAmount > 54) {
            p.sendMessage("§cThere seems to be a problem with the color gui. Please contact Strokkur24 (@strokkur24 on discord) " +
                    "and send him the following message: 'CONFIG-ERROR: 307 --- Expected a number less or equal than 54, found " + colorAmount + "'");
        }

        FastInv inv = new FastInv(getColumns(colorAmount) * 9, cfg.getString(name + ".title").replaceAll("&", "§"));

        if (cfg.getBoolean(name + ".enable-items")) {
            for (Object k : cfg.getKeys(true).stream().filter((str) -> str.contains(name + ".items.") && str.split("\\.").length == 3).toArray()) {
                String key = (String) k;

                if (cfg.getInt(key + "slot") > 9) {
                    continue;
                }

                Pair<ItemBuilder, Consumer<InventoryClickEvent>> item = getItem(key, p);
                inv.setItem(cfg.getInt(key + ".slot") + inv.getInventory().getSize() - 10, item.getKey().build(), item.getValue());
            }
        }

        int slot = 0;
        for (String c : ChatColorConfig.getAllColors()) {

            if (!ChatColorConfig.isVisible(p, c)) {
                continue;
            }

            ItemStack out;
            if (ChatColorConfig.hasData(c)) {
                out = new ItemStack(ChatColorConfig.getMaterial(c), 1, ChatColorConfig.getData(c));
            }
            else {
                out = new ItemStack(ChatColorConfig.getMaterial(c), 1);
            }

            ItemMeta meta = out.getItemMeta();
            meta.setDisplayName(replace(cfg.getString(name + ".colors-display"), p, c));

            List<String> lore = new ArrayList<>();
            Consumer<InventoryClickEvent> clickEvent = (e) -> {
                e.setCancelled(true);

                for (String str : cfg.getStringList(name + ".colors-messages.locked")) {
                    p.sendMessage(replace(str, p, c));
                }
            };

            if (p.hasPermission(ChatColorConfig.getPermission(c))) {
                for (String str : cfg.getStringList(name + ".colors-lore.owned")) {
                    str = replace(str, p, c);
                    lore.add(str);
                }
                clickEvent = (e) -> {
                    e.setCancelled(true);
                    Database.setChatColor(p.getUniqueId(), c);

                    for (String str : cfg.getStringList(name + ".colors-messages.equipped")) {
                        p.sendMessage(replace(str, p, c));
                    }
                };
            }
            else {
                for (String str : cfg.getStringList(name + ".colors-lore.locked")) {
                    str = replace(str, p, c);
                    lore.add(str);
                }
            }

            meta.setLore(lore);
            out.setItemMeta(meta);
            inv.setItem(slot, out, clickEvent);
            slot++;
        }

        return inv;
    }

    public static String replace(String raw, Player p, String color) {
        raw = raw.replaceAll("\\{generic_name}", ChatColorConfig.getGenericName(color));
        raw = raw.replaceAll("\\{color}", ChatColorConfig.getColor(color).replaceAll("&", "§"));
        raw = raw.replaceAll("\\{raw}", ChatColorConfig.getColor(color).replaceAll("§", "&").replaceAll("&", ".._;__.."));
        raw = raw.replaceAll("\\{obtained}", ChatColorConfig.getObtained(color));

        raw = PlaceholderAPI.setPlaceholders(p, raw);
        raw = raw.replaceAll("&", "§");

        return raw.replaceAll(Pattern.quote(".._;__.."), "&");
    }
}
