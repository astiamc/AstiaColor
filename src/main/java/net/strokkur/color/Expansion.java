package net.strokkur.color;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.strokkur.config.ColorConfig;
import net.strokkur.util.GetNickname;
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
        return "1.0-DEPLOY"; // As this is a local plugin, it does not matter
    }

    @Override
    public String onPlaceholderRequest(Player p, @NotNull final String msg) {

        if (msg.equalsIgnoreCase("selectedcolor")) {
            if (Database.doesNotContain(p.getUniqueId())) return "None";
            return Database.getColor(p.getUniqueId());
        }

        if (msg.equalsIgnoreCase("coloredcolor") || msg.equalsIgnoreCase("coloredcolour")) {
            if (Database.doesNotContain(p.getUniqueId())) return "None";

            String color = Database.getColor(p.getUniqueId());
            return ColorConfig.getColor(color).colorString(ColorConfig.getGenericName(color)).replaceAll("&", "§");
        }

        if (msg.equalsIgnoreCase("name")) {
            return ColorConfig.getColor(Database.getColor(p.getUniqueId())).colorString(GetNickname.get(p)).replaceAll("&", "§");
        }

        return null;
    }
}
