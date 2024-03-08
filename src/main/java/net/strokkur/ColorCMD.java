package net.strokkur;

import net.strokkur.config.ColorConfig;
import net.strokkur.config.GuiConfig;
import net.strokkur.util.MakeList;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ColorCMD implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("strokkur.reload")) {
            ColorConfig.reload();
            GuiConfig.reload();
            sender.sendMessage("§bSuccessfully reloaded §3§nColors.yml§b and §3§nGui.yml§b!");
            return true;
        }

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("§cYou can't do that!");
            return true;
        }

        Player p = (Player) sender;

        GuiConfig.getInventory("default", p).open(p);
        p.playSound(p.getLocation(), Sound.CHEST_OPEN, 1f, 1f);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return commandSender.hasPermission("strokkur.reload") && strings.length == 1 ? MakeList.of("reload") : new ArrayList<>();
    }
}
