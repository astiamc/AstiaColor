package net.strokkur.color;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
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

        return null;
    }
}
