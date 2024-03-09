package net.strokkur.color;

import net.strokkur.color.color.Database;
import net.strokkur.color.color.Expansion;
import net.strokkur.color.config.ChatColorConfig;
import net.strokkur.color.config.NameColorConfig;
import net.strokkur.color.config.GuiConfig;
import net.strokkur.color.util.fastinv.FastInvManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;

        Database.init();

        FastInvManager.register(this);
        new Expansion().register();

        NameColorConfig.init();
        ChatColorConfig.init();
        GuiConfig.init();

        getCommand("color").setExecutor(new ColorCMD());
        getCommand("color").setTabCompleter(new ColorCMD());
    }

    @Override
    public void onDisable() {
        new Expansion().unregister();
    }
}
