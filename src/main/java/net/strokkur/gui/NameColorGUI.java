package net.strokkur.gui;

import jdk.jfr.Name;
import net.strokkur.color.Database;
import net.strokkur.config.NameColorConfig;
import net.strokkur.util.Pair;
import net.strokkur.util.fastinv.FastInv;
import net.strokkur.util.fastinv.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static net.strokkur.config.GuiConfig.*;

public class NameColorGUI {

    public static FastInv get(Player p, String name) {
        int colorAmount = NameColorConfig.getAllColors().size();
        colorAmount += cfg.getBoolean(name + ".enable-items") ? 9 : 0;

        if (colorAmount > 54) {
            p.sendMessage("§cThere seems to be a problem with the color gui. Please contact Strokkur24 (@strokkur24 on discord) " +
                    "and send him the following message: 'CONFIG-ERROR: 207 --- Expected a number less or equal than 54, found " + colorAmount + "'");
        }

        FastInv inv = new FastInv(getColumns(colorAmount) * 9, getReplaced(name + ".title", p));

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
        for (String c : NameColorConfig.getAllColors()) {

            ItemStack out;
            if (NameColorConfig.hasData(c)) {
                out = new ItemStack(NameColorConfig.getMaterial(c), 1, NameColorConfig.getData(c));
            }
            else {
                out = new ItemStack(NameColorConfig.getMaterial(c), 1);
            }

            ItemMeta meta = out.getItemMeta();
            meta.setDisplayName(getReplaced(name + ".colors-display", p, c));

            Consumer<InventoryClickEvent> clickEvent = (e) -> {
                e.setCancelled(true);

                for (String str : cfg.getStringList(name + ".colors-messages.locked")) {
                    p.sendMessage(replace(str, p, c));
                }
            };

            List<String> lore = new ArrayList<>();
            if (p.hasPermission(NameColorConfig.getPermission(c))) {
                for (String str : cfg.getStringList(name + ".colors-lore.owned")) {
                    str = replace(str, p, c);
                    lore.add(str);
                }
                clickEvent = (e) -> {
                    e.setCancelled(true);
                    Database.setNameColor(p.getUniqueId(), c);

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

}
