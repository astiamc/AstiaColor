package net.strokkur.gui;

import net.strokkur.color.Database;
import net.strokkur.config.ColorConfig;
import net.strokkur.config.NameColor;
import net.strokkur.util.fastinv.FastInv;
import net.strokkur.util.fastinv.ItemBuilder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ColorGUI {

    public static void openGUI(Player p) {

        FastInv inv = new FastInv(54);

        // TODO: Add the items at the bottom

        int slot = 0;
        for (String color : ColorConfig.getAllColors()) {
            NameColor nameColor = ColorConfig.getColor(color);

            ItemBuilder builder = new ItemBuilder(ColorConfig.getMaterial(color));

            if (Database.contains(p.getUniqueId()) && Database.getColor(p.getUniqueId()).equals(color)) {
                builder.enchant(Enchantment.ARROW_INFINITE);
                builder.flags(ItemFlag.HIDE_ENCHANTS);
            }

            // replace {{somestring}} with the colored version of said string

            if (p.hasPermission(ColorConfig.getPermission(color))) {
                builder.lore(); // TODO: Lore for if you own the color
            }
            else {
                builder.lore(); // TODO: Lore for it you don't own the color
            }

            ItemStack item = builder.build();
            inv.setItem(slot, item, e -> {
                // TODO: Add logic for clicking on the namecolor item
            });

            slot++;
        }

        inv.open(p);
    }

}
