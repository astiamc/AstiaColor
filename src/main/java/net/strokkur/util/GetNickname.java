package net.strokkur.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GetNickname {

    public static String get(Player p) {
        return ChatColor.stripColor(PlaceholderAPI.setPlaceholders(p, "%essentials_nickname_stripped%"));
    }

}
