package net.strokkur;

import net.strokkur.config.GuiConfig;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ColorCMD implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("§cYou can't do that!");
            return true;
        }

        Player p = (Player) sender;

        GuiConfig.getInventory("default", p);
        p.playSound(p.getLocation(), Sound.CHEST_OPEN, 1f, 1f);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }
}
