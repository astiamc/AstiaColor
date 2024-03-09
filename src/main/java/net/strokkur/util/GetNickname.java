package net.strokkur.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GetNickname {

    public static String get(Player p) {
        String stripped = ChatColor.stripColor(PlaceholderAPI.setPlaceholders(p, "%essentials_nickname_stripped%"));
        if (stripped.equals(p.getName()))
            return stripped;
        return "~" + stripped;
    }

}
