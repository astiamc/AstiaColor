package net.strokkur.color;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.strokkur.config.ColorConfig;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Expansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "ac";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Strokkur24";
    }

    @Override
    public @NotNull String getVersion() {
        return "balls"; // As this is a local plugin, it does not matter
    }

    @Override
    public String onPlaceholderRequest(Player p, @NotNull final String msg) {

        if (msg.equalsIgnoreCase("selectedcolor")) {
            if (!Database.contains(p.getUniqueId())) return "None";
            return Database.getColor(p.getUniqueId());
        }

        if (msg.equalsIgnoreCase("coloredcolor") || msg.equalsIgnoreCase("coloredcolour")) {
            if (!Database.contains(p.getUniqueId()) || Database.getColor(p.getUniqueId()).equalsIgnoreCase("default"))
                return "None";

            String color = Database.getColor(p.getUniqueId());
            return ColorConfig.getColor(color).colorString(ColorConfig.getGenericName(color));
        }

        if (msg.equalsIgnoreCase("name")) {
            if (!Database.contains(p.getUniqueId())) return PlaceholderAPI.setPlaceholders(p, "%essentials_nickname%");
            return ColorConfig.getColor(Database.getColor(p.getUniqueId())).colorString(PlaceholderAPI.setPlaceholders(p, "%essentials_nickname%"));
        }

        return null;
    }
}
