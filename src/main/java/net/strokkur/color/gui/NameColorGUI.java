package net.strokkur.color.gui;

import net.strokkur.color.color.Database;
import net.strokkur.color.config.GuiConfig;
import net.strokkur.color.config.NameColorConfig;
import net.strokkur.color.util.Pair;
import net.strokkur.color.util.fastinv.FastInv;
import net.strokkur.color.util.fastinv.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NameColorGUI {

    public static FastInv get(Player p, String name) {
        int colorAmount = NameColorConfig.getAllColors().size();
        colorAmount += GuiConfig.cfg.getBoolean(name + ".enable-items") ? 9 : 0;

        if (colorAmount > 54) {
            p.sendMessage("§cThere seems to be a problem with the color gui. Please contact Strokkur24 (@strokkur24 on discord) " +
                    "and send him the following message: 'CONFIG-ERROR: 207 --- Expected a number less or equal than 54, found " + colorAmount + "'");
        }

        FastInv inv = new FastInv(GuiConfig.getColumns(colorAmount) * 9, GuiConfig.getReplaced(name + ".title", p));

        if (GuiConfig.cfg.getBoolean(name + ".enable-items")) {
            for (Object k : GuiConfig.cfg.getKeys(true).stream().filter((str) -> str.contains(name + ".items.") && str.split("\\.").length == 3).toArray()) {
                String key = (String) k;

                if (GuiConfig.cfg.getInt(key + "slot") > 9) {
                    continue;
                }

                Pair<ItemBuilder, Consumer<InventoryClickEvent>> item = GuiConfig.getItem(key, p);
                inv.setItem(GuiConfig.cfg.getInt(key + ".slot") + inv.getInventory().getSize() - 10, item.getKey().build(), item.getValue());
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
            meta.setDisplayName(GuiConfig.getReplaced(name + ".colors-display", p, c));

            Consumer<InventoryClickEvent> clickEvent = (e) -> {
                e.setCancelled(true);

                for (String str : GuiConfig.cfg.getStringList(name + ".colors-messages.locked")) {
                    p.sendMessage(GuiConfig.replace(str, p, c));
                }
            };

            List<String> lore = new ArrayList<>();
            if (p.hasPermission(NameColorConfig.getPermission(c))) {
                for (String str : GuiConfig.cfg.getStringList(name + ".colors-lore.owned")) {
                    str = GuiConfig.replace(str, p, c);
                    lore.add(str);
                }
                clickEvent = (e) -> {
                    e.setCancelled(true);
                    Database.setNameColor(p.getUniqueId(), c);

                    for (String str : GuiConfig.cfg.getStringList(name + ".colors-messages.equipped")) {
                        p.sendMessage(GuiConfig.replace(str, p, c));
                    }
                };
            }
            else {
                for (String str : GuiConfig.cfg.getStringList(name + ".colors-lore.locked")) {
                    str = GuiConfig.replace(str, p, c);
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
