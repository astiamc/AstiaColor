package net.strokkur.color;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.strokkur.config.ChatColorConfig;
import net.strokkur.config.NameColorConfig;
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

        if (msg.equalsIgnoreCase("selected_namecolor")) {
            if (Database.hasNoNameColor(p.getUniqueId())) return "None";

            String color = Database.getNameColor(p.getUniqueId());
            return NameColorConfig.getGenericName(color);
        }

        if (msg.equalsIgnoreCase("selected_namecolor_applied")) {
            if (Database.hasNoNameColor(p.getUniqueId())) return "None";

            String color = Database.getNameColor(p.getUniqueId());
            return NameColorConfig.getColor(color).colorString(NameColorConfig.getGenericName(color)).replaceAll("&", "§");
        }

        if (msg.equalsIgnoreCase("name")) {
            return NameColorConfig.getColor(Database.getNameColor(p.getUniqueId())).colorString(GetNickname.get(p)).replaceAll("&", "§");
        }


        if (msg.equalsIgnoreCase("selected_chatcolor")) {
            if (Database.hasNoNameColor(p.getUniqueId())) return "None";

            String color = Database.getChatColor(p.getUniqueId());
            return ChatColorConfig.getGenericName(color);
        }

        if (msg.equalsIgnoreCase("color")) {
            return ChatColorConfig.getColor(Database.getChatColor(p.getUniqueId())).replaceAll("&", "§");
        }

        return null;
    }
}
